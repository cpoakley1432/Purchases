package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
/**
 * Created by cameronoakley on 11/11/15.
 */
@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;

    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init(){
        if (customers.count() == 0) {
            String fileContentCust = readFile("customers.csv");
            String[] linesCust = fileContentCust.split("\n");

            for (String lineCust : linesCust) {
                if (lineCust == linesCust[0])
                    continue;
                String[] columns = lineCust.split(",");
                Customer customer = new Customer();
                customer.name = columns[0];
                customer.email = columns[1];
                customers.save(customer);
            }
        }
        if (purchases.count() == 0) {
            String fileContentPurc = readFile("purchases.csv");
            String[] linesPurc = fileContentPurc.split("\n");

            for (String linePurc : linesPurc) {
                if (linePurc == linesPurc[0])
                    continue;
                String[] columns2 = linePurc.split(",");
                Purchase purchase = new Purchase();
                purchase.date = columns2[0];
                purchase.creditCard = columns2[1];
                purchase.cvv = columns2[2];
                purchase.category = columns2[3];
                purchases.save(purchase);
            }
        }
    }

    @RequestMapping ("/")
    public String home (Model model){
        Iterable<Purchase> purchaseList = purchases.findAll();
        model.addAttribute("purchaseList" , purchaseList);
        return "home";
    }


    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}
