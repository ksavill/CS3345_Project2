from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from dearpygui import dearpygui as dpg
import os
import csv
import pyperclip
# potential resource for improving this script https://www.isbndb.com/apidocs/v2
main_content = []
class sel:
    def get_value(driver,element_type,element,attribute):
        if element_type in ['xpath']:
            element_value = driver.find_element(By.XPATH,element).get_attribute(attribute)
            return element_value
    def value(driver,element_type,element):
        if element_type in ['class_name']:
            element_value = driver.find_element(By.CLASS_NAME,element).text()
            
            return element_value
        if element_type in ['xpath']:
            element_value = driver.find_element(By.XPATH,element)
            print(element_value)
            element_value = element_value.text()
            return element_value

    def env(driver,env_url):
        driver.get(env_url)
    
    def get_env(driver):
        current_env = driver.current_url
        return current_env
    
    def refresh_env(driver):
        driver.refresh()

    def click(driver,element_type,element):
        if element_type in ['id']:
            driver.find_element(By.ID,element).click()

        if element_type in ['class']:
            driver.find_element(By.CLASS_NAME,element).click()

        if element_type in ['name']:
            driver.find_element(By.NAME,element).click()

        if element_type in ['xpath']:
            driver.find_element(By.XPATH,element).click()

        if element_type in ['css']:
            driver.find_element(By.CSS_SELECTOR,element).click()

    def type(driver,element_type,element,data):
        if element_type in ['id']:
            driver.find_element(By.ID,element).send_keys(data)
        if element_type in ['class']:
            driver.find_element(By.CLASS_NAME,element).send_keys(data)

        if element_type in ['name']:
            driver.find_element(By.NAME,element).send_keys(data)

        if element_type in ['xpath']:
            driver.find_element(By.XPATH,element).send_keys(data)

        if element_type in ['css']:
            driver.find_element(By.CSS_SELECTOR,element).send_keys(data)

    def clear(driver,element_type,element):
        if element_type in ['id']:
            driver.find_element(By.ID,element).clear()

        if element_type in ['name']:
            driver.find_element(By.CLASS_NAME,element).clear()

        if element_type in ['xpath']:
            driver.find_element(By.XPATH,element).clear()

        if element_type in ['css']:
            driver.find_element(By.CSS_SELECTOR,element).clear()

    def find(driver,element_type,element):
        if element_type in ['id']:
            driver.find_element(By.ID,element)
        if element_type in ['xpath']:
            driver.find_element(By.XPATH,element)

class Menus:
    def main_menu():
        if dpg.does_item_exist("main_menu"):
            dpg.show_item("main_menu")
        else:
            with dpg.window(label="BookGrabber Menu",tag="main_menu",width=800,height=1000):
                dpg.add_text("Book Grabber Automation, website this pulls from is https://isbnsearch.org/")
                dpg.add_text("")
                with dpg.group(horizontal=True):
                    dpg.add_text("Book Search Prefix: ")
                    dpg.add_input_text(tag="searchPrefix",width=100)
                with dpg.group(horizontal=True):
                    dpg.add_text("Amount of books: ")
                    dpg.add_text("10")
                with dpg.group(horizontal=True):
                    dpg.add_text("Export Filename: ")
                    dpg.add_input_text(tag="fileName",default_value="booklist",width=100)
                dpg.add_text("Filetype Exports:")
                with dpg.group(horizontal=True):
                    dpg.add_text(".txt:")
                    dpg.add_checkbox(tag="exportTXT",default_value=True)
                with dpg.group(horizontal=True):
                    dpg.add_text(".csv:")
                    dpg.add_checkbox(tag="exportCSV",default_value=True)
                dpg.add_text("")
                dpg.add_button(label="Start Book Grabbing Automation",callback=Automation.inputValidation)
                dpg.add_text("\n\n")
                dpg.add_button(label="Copy results to clipboard",callback=Menus.copyToClipboard)
    
    def copyToClipboard():
        global main_content
        copy_string = "\n".join("".join(book) for book in main_content)
        pyperclip.copy(copy_string)
        print("String copied.")
                    
class Automation:
    def warningPopup(popuptext):
        if dpg.does_item_exist("warningPopup"):
            Automation.deleteWindow()
        with dpg.window(label="Error",tag="warningPopup"):
            dpg.add_text(popuptext,tag="warningText")
            dpg.add_button(label="Okay",callback=Automation.deleteWindow)
            
    def deleteWindow():
        dpg.delete_item("warningPopup")
    
    def inputValidation():
        global fileName,exportTXT,exportCSV,main_content
        searchPrefix = dpg.get_value("searchPrefix")
        fileName = dpg.get_value("fileName")
        exportTXT = dpg.get_value("exportTXT")
        exportCSV = dpg.get_value("exportCSV")
        if len(searchPrefix) < 1 or len(fileName) < 1 or (not exportTXT and not exportCSV):
            Automation.warningPopup("Invalid input.")
            return
        print("Inputs validated, beginning webdriver automation.")
        main_content = Automation.getBooks(searchPrefix)
        if not main_content:
            print("Not continuing to export files.")
            return
        if exportTXT:
            print("Exporting as .txt")
            Exporting.exportTXT(main_content, fileName)
        if exportCSV:
            print("Exporting as .csv")
            Exporting.exportCSV(filename=fileName, fields=['ISBN', 'Title', 'Author'], content=main_content)

    def getBooks(searchPrefix):
        global driver
        driver = WebDriver.start_driver()
        booklist = []
        subbooklist = []
        sel.env(driver,'https://isbnsearch.org/')
        sel.clear(driver,'xpath',"/html/body/div[1]/form/div/input")
        sel.type(driver,'xpath',"/html/body/div[1]/form/div/input",searchPrefix)
        sel.type(driver,'xpath',"/html/body/div[1]/form/div/input",Keys.RETURN)
        try:
            sel.find(driver,'xpath',"/html/body/div[1]/div/h1")
            print("Captcha verification detected.")
            continueprompt = input("press enter to continue (once you have gone through the captcha)")
        except:
            pass
        try:
            for i in range(9):
                print("Book " + str(i+1))
                subbooklist = []
                bookisbn = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i+1)+"]/div[2]/p[2]","innerText")
                bookisbn = bookisbn.replace("ISBN-13: ","")
                subbooklist.append(bookisbn)
                booktitle = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i+1)+"]/div[2]/h2/a","innerText")
                subbooklist.append(booktitle)
                bookauthor = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i+1)+"]/div[2]/p[1]","innerText")
                bookauthor = bookauthor.replace("Author: ","")
                subbooklist.append(bookauthor)
                booklist.append(subbooklist)
                # /html/body/div/ul/li[8]/div[2]/h2
                # /html/body/div/ul/li[8]/div[2]/p[1]
                # //*[@id="searchresults"]/li[8]/div[2]/p[2]

                # /html/body/div/ul/li[7]/div[2]/p[2]

            print("pressing next button")
            sel.click(driver,'xpath',"/html/body/div[1]/p/a[2]")
            try:
                sel.find(driver,'xpath',"/html/body/div[1]/div/h1")
                print("Captcha verification detected.")
                continueprompt = input("press enter to continue (once you have gone through the captcha)")
            except:
                pass
            subbooklist=[]
            i=1
            bookisbn = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i)+"]/div[2]/p[2]","innerText")
            bookisbn = bookisbn.replace("ISBN-13: ","")
            print(bookisbn)
            subbooklist.append(bookisbn)
            booktitle = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i)+"]/div[2]/h2/a","innerText")
            subbooklist.append(booktitle)
            print(booktitle)
            bookauthor = sel.get_value(driver,'xpath',"/html/body/div[1]/ul/li["+str(i)+"]/div[2]/p[1]","innerText")
            bookauthor = bookauthor.replace("Author: ","")
            print(bookauthor)
            subbooklist.append(bookauthor)
            booklist.append(subbooklist)
            print(booklist)
            print("book stuff grabbed.")
            
        except:
            print("There was an issue getting the book list for this prompt, please select another search term and try again.")
            booklist = []
        return booklist 

        
    
    
    
class WebDriver:
    def start_driver():
        global driver
        driver = webdriver.Chrome(r'./ChromeDriver/Windows/chromedriver.exe')
        return driver

class Exporting:
    def exportTXT(main_content, fileName):
        print("Exporting book list to TXT file")
        fileName = fileName.replace('.txt', '').replace('.csv', '') + '.txt'
        try:
            with open(fileName, 'w') as file:
                for book in main_content:
                    for item in book:
                        file.write(f'{item}\n')
        except:
            print("There was an issue with exporting the TXT file.")

    def exportCSV(filename="", fields=[], content=[], extradirectory=""):
        print("Exporting to CSV file")
        fileName = filename.replace('.txt', '').replace('.csv', '') + '.csv'
        try:
            with open(fileName, 'w', newline='') as file:
                write = csv.writer(file)
                write.writerow(fields)
                write.writerows(content)
        except:
            print("There was an issue with exporting the CSV file.")



# start_driver()
# getprompt()

if __name__ == '__main__':
    dpg.create_context()
    dpg.create_viewport(title="CS3345 BookGrabber Automation - Kevin Savill",width=800,height=1000)
    dpg.setup_dearpygui()


    Menus.main_menu()

    #this goes at the very end of the script
    dpg.show_viewport()
    dpg.start_dearpygui()
    dpg.destroy_context()