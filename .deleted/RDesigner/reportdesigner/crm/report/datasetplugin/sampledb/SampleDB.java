/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb;

import org.jetbrains.annotations.NonNls;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCClassLoader;

import java.sql.Connection;

/**
 * User: Martin
 * Date: 17.02.2006
 * Time: 13:09:49
 */
@NonNls
public class SampleDB
{
    private SampleDB()
    {
    }


    @SuppressWarnings({"HardCodedStringLiteral"})
    public static void initSampleDB() throws Exception
    {
        Connection c = JDBCClassLoader.getConnection("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:sample", "sa", "");

        //Class.forName("org.hsqldb.jdbcDriver");

        //noinspection JDBCResourceOpenedButNotSafelyClosed
        //Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:sample", "sa", "");

        c.createStatement().executeUpdate("CREATE MEMORY TABLE " +
                                          "addresses (" +
                                          "address_id INT NOT NULL, " +
                                          "addressline1 VARCHAR NOT NULL, " +
                                          "addressline2 VARCHAR, " +
                                          "zip_code INT NOT NULL, " +
                                          "city VARCHAR NOT NULL, " +
                                          "PRIMARY KEY(address_id) " +
                                          ")");

        c.createStatement().executeUpdate("CREATE MEMORY TABLE " +
                                          "customers (" +
                                          "customer_id INT NOT NULL, " +
                                          "first_name VARCHAR NOT NULL, " +
                                          "last_name VARCHAR NOT NULL, " +
                                          "address_id INT NOT NULL, " +
                                          "PRIMARY KEY(customer_id), " +
                                          "FOREIGN KEY (address_id) REFERENCES addresses(address_id) " +
                                          ")");

        c.createStatement().executeUpdate("CREATE MEMORY TABLE " +
                                          "orders (" +
                                          "order_id INT NOT NULL, " +
                                          "order_date TIMESTAMP NOT NULL, " +
                                          "shipping_date TIMESTAMP, " +
                                          "customer_id INT NOT NULL, " +
                                          "PRIMARY KEY(order_id), " +
                                          "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
                                          ")");

        c.createStatement().executeUpdate("CREATE MEMORY TABLE " +
                                          "products (" +
                                          "product_id INT NOT NULL, " +
                                          "product_name VARCHAR NOT NULL, " +
                                          "product_description VARCHAR, " +
                                          "price DECIMAL NOT NULL, " +
                                          "PRIMARY KEY(product_id) " +
                                          ")");

        c.createStatement().executeUpdate("CREATE MEMORY TABLE " +
                                          "order_items (" +
                                          "order_item_id INT NOT NULL, " +
                                          "order_id INT NOT NULL, " +
                                          "product_id INT NOT NULL, " +
                                          "PRIMARY KEY(order_item_id), " +
                                          "FOREIGN KEY (order_id) REFERENCES orders(order_id), " +
                                          "FOREIGN KEY (product_id) REFERENCES products(product_id)" +
                                          ")");


        c.createStatement().executeUpdate("INSERT INTO addresses (address_id, addressline1, addressline2, zip_code, city) VALUES (1, 'Bernstrasse 5', null, 3000, 'Bern')");
        c.createStatement().executeUpdate("INSERT INTO addresses (address_id, addressline1, addressline2, zip_code, city) VALUES (2, 'Schwanengasse 5+7', null, 3200, 'Biel')");
        c.createStatement().executeUpdate("INSERT INTO addresses (address_id, addressline1, addressline2, zip_code, city) VALUES (3, 'Elsterweg 17', null, 5134, 'Gurzelen')");

        c.createStatement().executeUpdate("INSERT INTO customers (customer_id, first_name, last_name, address_id) VALUES (1, 'Hugo', 'Habicht', 1)");
        c.createStatement().executeUpdate("INSERT INTO customers (customer_id, first_name, last_name, address_id) VALUES (2, 'Hans', 'Meiser', 2)");
        c.createStatement().executeUpdate("INSERT INTO customers (customer_id, first_name, last_name, address_id) VALUES (3, 'Hans', 'Müller', 2)");
        c.createStatement().executeUpdate("INSERT INTO customers (customer_id, first_name, last_name, address_id) VALUES (4, 'Erik', 'Brown', 3)");
        c.createStatement().executeUpdate("INSERT INTO customers (customer_id, first_name, last_name, address_id) VALUES (5, 'Patrick', 'Simpson', 2)");

        c.createStatement().executeUpdate("INSERT INTO orders (order_id, order_date, shipping_date, customer_id) VALUES (1, '2005-12-23', null        , 1)");
        c.createStatement().executeUpdate("INSERT INTO orders (order_id, order_date, shipping_date, customer_id) VALUES (2, '2005-01-15', '2005-01-19', 2)");
        c.createStatement().executeUpdate("INSERT INTO orders (order_id, order_date, shipping_date, customer_id) VALUES (3, '2005-03-17', '2005-03-23', 3)");
        c.createStatement().executeUpdate("INSERT INTO orders (order_id, order_date, shipping_date, customer_id) VALUES (4, '2005-07-27', '2005-08-2', 4)");
        c.createStatement().executeUpdate("INSERT INTO orders (order_id, order_date, shipping_date, customer_id) VALUES (5, '2005-07-28', '2005-08-3', 5)");

        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (1, 'Matrix', null, 15.98)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (2, 'Mogli', null, 23.98)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (3, 'Electra', null, 9.65)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (4, 'Hell Boy', null, 17.30)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (5, 'Harry Potter', null, 6.95)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (6, 'Incredibles', 'After creating the last great traditionally animated film of the 20th century, The Iron Giant, filmmaker Brad Bird joined top-drawer studio Pixar to create this exciting, completely entertaining computer-animated film. Bird gives us a family of \"supers,\" a brood of five with special powers desperately trying to fit in with the 9-to-5 suburban lifestyle. Of course, in a more innocent world, Bob and Helen Parr were superheroes, Mr. Incredible and Elastigirl. But blasted lawsuits and public disapproval forced them and other supers to go incognito, making it even tougher for their school-age kids, the shy Violet and the aptly named Dash. When a stranger named Mirage (voiced by Elizabeth Pena) secretly recruits Bob for a potential mission, the old glory days spin in his head, even if his body is a bit too plump for his old super suit.', 14.97)");
        c.createStatement().executeUpdate("INSERT INTO products (product_id, product_name, product_description, price) VALUES (7, 'Lost', 'Along with Desperate Housewives, Lost was one of the two breakout shows in the fall of 2004. Mixing suspense and action with a sci-fi twist, it began with a thrilling pilot episode in which a jetliner traveling from Australia to Los Angeles crashes, leaving 48 survivors on an unidentified island with no sign of civilization or hope of imminent rescue. That may sound like Gilligans Island meets Survivor, but Lost kept viewers tuning in every Wednesday night--and spending the rest of the week speculating on Web sites--with some irresistible hooks (not to mention the beautiful women). First, theres a huge ensemble cast of no fewer than 14 regular characters, and each episode fills in some of the back story on one of them. Theres a doctor; an Iraqi soldier; a has-been rock star; a fugitive from justice; a self-absorbed young woman and her brother; a lottery winner; a father and son; a Korean couple; a pregnant woman; and others. Second, theres a host of unanswered questions: What is the mysterious beast that lurks in the jungle? Why do polar bears and wild boars live there? Why has a woman been transmitting an SOS message in French from somewhere on the island for the last 16 years? Why do impossible wishes seem to come true? Are they really on a physical island, or somewhere else? What is the significance of the recurring set of numbers? And will Kate ever give up her bad-boy fixation and hook up with Jack?', 38.99)");

        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 1, 1, 1)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 2, 1, 2)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 3, 1, 3)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 4, 1, 4)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 5, 1, 5)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 6, 2, 1)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 7, 2, 3)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 8, 2, 5)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES ( 9, 3, 1)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (10, 3, 2)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (11, 3, 3)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (12, 4, 1)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (13, 4, 2)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (14, 4, 3)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (15, 4, 4)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (16, 4, 5)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (17, 4, 6)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (18, 4, 7)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (19, 5, 1)");
        c.createStatement().executeUpdate("INSERT INTO order_items (order_item_id, order_id, product_id) VALUES (20, 5, 2)");

        /*
SELECT
CUSTOMERS.FIRST_NAME, CUSTOMERS.LAST_NAME,
PRODUCTS.PRODUCT_NAME, PRODUCTS.PRODUCT_DESCRIPTION, PRODUCTS.PRICE
FROM CUSTOMERS
JOIN ORDERS ON CUSTOMERS.CUSTOMER_ID=ORDERS.CUSTOMER_ID
JOIN ORDER_ITEMS ON ORDER_ITEMS.ORDER_ID=ORDERS.ORDER_ID
JOIN PRODUCTS ON ORDER_ITEMS.PRODUCT_ID=PRODUCTS.PRODUCT_ID
ORDER BY
CUSTOMERS.FIRST_NAME, PRODUCTS.PRODUCT_NAME
        */

    }
}
