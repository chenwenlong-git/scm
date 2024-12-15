update purchase_child_order AS pco
    INNER JOIN purchase_parent_order AS ppo
    ON pco.purchase_parent_order_no = ppo.purchase_parent_order_no
set pco.sku_type = ppo.sku_type;


update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "71"
where supplier_code = "5"
  and sku = "K5-134C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "43"
where supplier_code = "5"
  and sku = "K5-134C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "112"
where supplier_code = "5"
  and sku = "K5-134C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "142"
where supplier_code = "5"
  and sku = "K5-134C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "88"
where supplier_code = "5"
  and sku = "K5-134C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "58"
where supplier_code = "5"
  and sku = "K5-134C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "5"
where supplier_code = "5"
  and sku = "K5-134C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "433"
where supplier_code = "5"
  and sku = "K5-134LC-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "468"
where supplier_code = "5"
  and sku = "K5-134LC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "333"
where supplier_code = "5"
  and sku = "K5-134LC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "181"
where supplier_code = "5"
  and sku = "K5-134LC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "284"
where supplier_code = "5"
  and sku = "K5-134LC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "38"
where supplier_code = "5"
  and sku = "K5-134LC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "111"
where supplier_code = "5"
  and sku = "K5-134LC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "3",
    self_provide_inventory = "0"
where supplier_code = "5"
  and sku = "K5-134LC-YK-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "66"
where supplier_code = "5"
  and sku = "K5-136C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "165"
where supplier_code = "5"
  and sku = "K5-136C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "236"
where supplier_code = "5"
  and sku = "K5-136C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "154"
where supplier_code = "5"
  and sku = "K5-136C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "44"
where supplier_code = "5"
  and sku = "K5-136C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "45"
where supplier_code = "5"
  and sku = "K5-136C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "51"
where supplier_code = "5"
  and sku = "K5-136LC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "85"
where supplier_code = "5"
  and sku = "K5-136LC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "132"
where supplier_code = "5"
  and sku = "K5-136LC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "115"
where supplier_code = "5"
  and sku = "K5-136LC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "146"
where supplier_code = "5"
  and sku = "K5-136LC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "114"
where supplier_code = "5"
  and sku = "K5-136LC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "49"
where supplier_code = "5"
  and sku = "K5-136LC-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "10"
where supplier_code = "5"
  and sku = "K5-360C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "22"
where supplier_code = "5"
  and sku = "K5-360C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "40"
where supplier_code = "5"
  and sku = "K5-360C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "40"
where supplier_code = "5"
  and sku = "K5-360C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "30"
where supplier_code = "5"
  and sku = "K5-360C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "10"
where supplier_code = "5"
  and sku = "K5-360C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "31"
where supplier_code = "5"
  and sku = "K5-360LC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "37"
where supplier_code = "5"
  and sku = "K5-360LC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "10"
where supplier_code = "5"
  and sku = "K5-360LC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "17"
where supplier_code = "5"
  and sku = "K5-360LC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "2398"
where supplier_code = "5"
  and sku = "K5-55C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "220"
where supplier_code = "5"
  and sku = "K5-55C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "325"
where supplier_code = "5"
  and sku = "K5-55C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "425"
where supplier_code = "5"
  and sku = "K5-55C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "217"
where supplier_code = "5"
  and sku = "K5-55C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "196"
where supplier_code = "5"
  and sku = "K5-55C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "73"
where supplier_code = "5"
  and sku = "K5-55C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "691"
where supplier_code = "5"
  and sku = "K5-55LC-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "28"
where supplier_code = "5"
  and sku = "K5-55LC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "408"
where supplier_code = "5"
  and sku = "K5-55LC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "20"
where supplier_code = "5"
  and sku = "K5-55LC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "158",
    self_provide_inventory = "0"
where supplier_code = "5"
  and sku = "K5-55LC-ST-16-F-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "784"
where supplier_code = "5"
  and sku = "K5-55LC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "94",
    self_provide_inventory = "0"
where supplier_code = "5"
  and sku = "K5-55LC-ST-18-F-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "534"
where supplier_code = "5"
  and sku = "K5-55LC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "175",
    self_provide_inventory = "0"
where supplier_code = "5"
  and sku = "K5-55LC-ST-20-F-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "579"
where supplier_code = "5"
  and sku = "K5-55LC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "319"
where supplier_code = "5"
  and sku = "K5-55LC-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "46"
where supplier_code = "5"
  and sku = "K5-FULC-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "70"
where supplier_code = "5"
  and sku = "K5-FULC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "48"
where supplier_code = "5"
  and sku = "K5-FULC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "70"
where supplier_code = "5"
  and sku = "K5-FULC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "27"
where supplier_code = "5"
  and sku = "K5-FULC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "30"
where supplier_code = "5"
  and sku = "K5-FULC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "1792",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "802",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "56",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "6",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "231",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "145",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "9",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "3665",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "349",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "519",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "272",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "869",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-134-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "523",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-360-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "566",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-360-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "819",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-360-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "383",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-360-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "248",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-360-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "977",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "325",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "308",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "6",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "786",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "182",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "210",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "14",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "1",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-44-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "795",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-12-C-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "758",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-12-M-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "753",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-14-C-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "759",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-14-M-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "453",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-16-C-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "547",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-16-M-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "447",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-18-C-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "476",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-18-M-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "299",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-20-C-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "300",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55C-ST-20-M-NC-LLK";
update supplier_inventory
set stock_up_inventory     = "1185",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "3971",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "5533",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "699",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "6522",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "271",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55L-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "1462",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "334",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "1123",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "1649",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "1699",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-55-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "36",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-76C-ST-16-C-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "602",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "336",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "216",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "475",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "8",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "100",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "109",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-FU-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "83",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-X1L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "276",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-08-C-NC";
update supplier_inventory
set stock_up_inventory     = "544",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-10-C-NC";
update supplier_inventory
set stock_up_inventory     = "35",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "302",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "158",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-14-C-NC";
update supplier_inventory
set stock_up_inventory     = "311",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-14-M-NC";
update supplier_inventory
set stock_up_inventory     = "62",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "301",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K5-XTL-ST-18-M-NC";
update supplier_inventory
set stock_up_inventory     = "1949",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-44-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "1628",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "2044",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-44-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "229",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "237",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "665",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-X1C-YK-08-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "118",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-X1C-YK-12-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "487",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-XTL-ST-06-C-NC";
update supplier_inventory
set stock_up_inventory     = "8",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-XTL-ST-08-C-NC";
update supplier_inventory
set stock_up_inventory     = "48",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K7-XTL-ST-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "232",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K8-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "86",
    self_provide_inventory = "0"
where supplier_code = "8"
  and sku = "K8-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "76"
where supplier_code = "51"
  and sku = "K5-134L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "281"
where supplier_code = "51"
  and sku = "K5-134L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "526"
where supplier_code = "51"
  and sku = "K5-134L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "162"
where supplier_code = "51"
  and sku = "K5-134L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "3"
where supplier_code = "51"
  and sku = "K5-134L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "10"
where supplier_code = "51"
  and sku = "K5-134-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "940"
where supplier_code = "51"
  and sku = "K5-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "692"
where supplier_code = "51"
  and sku = "K5-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "762"
where supplier_code = "51"
  and sku = "K5-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "723"
where supplier_code = "51"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "47"
where supplier_code = "51"
  and sku = "K5-134-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "30"
where supplier_code = "51"
  and sku = "K5-136-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "87"
where supplier_code = "51"
  and sku = "K5-136-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "305"
where supplier_code = "51"
  and sku = "K5-136-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "308"
where supplier_code = "51"
  and sku = "K5-136-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "158"
where supplier_code = "51"
  and sku = "K5-136-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "71"
where supplier_code = "51"
  and sku = "K5-136-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "402"
where supplier_code = "51"
  and sku = "K5-360-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "465"
where supplier_code = "51"
  and sku = "K5-360-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "49"
where supplier_code = "51"
  and sku = "K5-360-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "215"
where supplier_code = "51"
  and sku = "K5-360-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "30"
where supplier_code = "51"
  and sku = "K5-360-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "14"
where supplier_code = "51"
  and sku = "K5-360-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "63"
where supplier_code = "51"
  and sku = "K5-44L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "49"
where supplier_code = "51"
  and sku = "K5-44L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "484"
where supplier_code = "51"
  and sku = "K5-44L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "462"
where supplier_code = "51"
  and sku = "K5-44L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "498"
where supplier_code = "51"
  and sku = "K5-44L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "409"
where supplier_code = "51"
  and sku = "K5-44L-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "48"
where supplier_code = "51"
  and sku = "K5-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1"
where supplier_code = "51"
  and sku = "K5-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "115"
where supplier_code = "51"
  and sku = "K5-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "45"
where supplier_code = "51"
  and sku = "K5-55L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "906"
where supplier_code = "51"
  and sku = "K5-55L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "500"
where supplier_code = "51"
  and sku = "K5-55L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "124"
where supplier_code = "51"
  and sku = "K5-55L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "315"
where supplier_code = "51"
  and sku = "K5-55L-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "439"
where supplier_code = "51"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "138"
where supplier_code = "51"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "340"
where supplier_code = "51"
  and sku = "K5-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "712"
where supplier_code = "51"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "526"
where supplier_code = "51"
  and sku = "K5-55-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "363"
where supplier_code = "51"
  and sku = "K5-55-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "6"
where supplier_code = "51"
  and sku = "K5-FU-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "113"
where supplier_code = "51"
  and sku = "K5-FU-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "153"
where supplier_code = "51"
  and sku = "K5-FU-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "76"
where supplier_code = "51"
  and sku = "K5-FU-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "151"
where supplier_code = "51"
  and sku = "K5-FU-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "160"
where supplier_code = "51"
  and sku = "K5-FU-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "35"
where supplier_code = "51"
  and sku = "K5-FU-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "23",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "253",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "103",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "30",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "145",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "584",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "344",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "512",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-134-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "4",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "136",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-14-F-350";
update supplier_inventory
set stock_up_inventory     = "381",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-14-F-613";
update supplier_inventory
set stock_up_inventory     = "816",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "70",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-16-F-350";
update supplier_inventory
set stock_up_inventory     = "222",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-16-F-613";
update supplier_inventory
set stock_up_inventory     = "192",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "306",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-18-F-613";
update supplier_inventory
set stock_up_inventory     = "599",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "130",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-20-F-350";
update supplier_inventory
set stock_up_inventory     = "203",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "496",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "205",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55L-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "283",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "229",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "150",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "435",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "513",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "178",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "45",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "100",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K6-55-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "600",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K8-55L-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "100",
    self_provide_inventory = "0"
where supplier_code = "51"
  and sku = "K8-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "15",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-16-S-DP2";
update supplier_inventory
set stock_up_inventory     = "84",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-16-S-MB30";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-20-S-HZ";
update supplier_inventory
set stock_up_inventory     = "30",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-20-S-MB30";
update supplier_inventory
set stock_up_inventory     = "5",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-24-S-DP2";
update supplier_inventory
set stock_up_inventory     = "7",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-24-S-HZ";
update supplier_inventory
set stock_up_inventory     = "97",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-55LC-LW-24-S-MB30";
update supplier_inventory
set stock_up_inventory     = "754",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-LW-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "83",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-LW-12-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "419",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-WW-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "1532",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-WW-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "187",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-YK-18-M-NC";
update supplier_inventory
set stock_up_inventory     = "100",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1C-YK-18-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "90",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-X1-YK-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "536",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-EGG-08-C-M228";
update supplier_inventory
set stock_up_inventory     = "537",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-EGG-08-C-NC";
update supplier_inventory
set stock_up_inventory     = "215",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-EGG-12-C-M228";
update supplier_inventory
set stock_up_inventory     = "525",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-EGG-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "500",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-KC-08-S-NC";
update supplier_inventory
set stock_up_inventory     = "1381",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-KC-08-S-TB4";
update supplier_inventory
set stock_up_inventory     = "80",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-KC-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "69",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-KC-12-S-TB4";
update supplier_inventory
set stock_up_inventory     = "502",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-LW-10-M-A99J";
update supplier_inventory
set stock_up_inventory     = "121",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-LW-10-M-M228";
update supplier_inventory
set stock_up_inventory     = "383",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-LW-12-M-A99J";
update supplier_inventory
set stock_up_inventory     = "943",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-LW-12-M-M228";
update supplier_inventory
set stock_up_inventory     = "739",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-LW-14-M-M228";
update supplier_inventory
set stock_up_inventory     = "390",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-PIX-06-C-613";
update supplier_inventory
set stock_up_inventory     = "60",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-PIX-06-C-NC";
update supplier_inventory
set stock_up_inventory     = "1584",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-ST-10-S-T228";
update supplier_inventory
set stock_up_inventory     = "109",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-ST-12-S-NC";
update supplier_inventory
set stock_up_inventory     = "3192",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-YK-10-C-NC";
update supplier_inventory
set stock_up_inventory     = "2977",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-YK-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-YK-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "1220",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K5-XTLC-YK-14-C-NC";
update supplier_inventory
set stock_up_inventory     = "1129",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-44LC-YK-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "29",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "51",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-ST-12-M-2";
update supplier_inventory
set stock_up_inventory     = "37",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-10-M-A99J";
update supplier_inventory
set stock_up_inventory     = "323",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-10-M-A99J-LK";
update supplier_inventory
set stock_up_inventory     = "242",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-10-M-MB30-LK";
update supplier_inventory
set stock_up_inventory     = "331",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "41",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-10-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "42",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-12-M-A99J-LK";
update supplier_inventory
set stock_up_inventory     = "327",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-12-M-MB30-LK";
update supplier_inventory
set stock_up_inventory     = "1397",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "90",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-12-M-NC-LK";
update supplier_inventory
set stock_up_inventory     = "49",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-14-M-A99J-LK";
update supplier_inventory
set stock_up_inventory     = "340",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-14-M-MB30-LK";
update supplier_inventory
set stock_up_inventory     = "27",
    self_provide_inventory = "0"
where supplier_code = "52"
  and sku = "K7-X1C-YK-14-M-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "146"
where supplier_code = "54"
  and sku = "K5-134L-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "5"
where supplier_code = "54"
  and sku = "K5-134L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "70"
where supplier_code = "54"
  and sku = "K5-134L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "8"
where supplier_code = "54"
  and sku = "K5-134L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "5"
where supplier_code = "54"
  and sku = "K5-134L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "183"
where supplier_code = "54"
  and sku = "K5-134L-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "8"
where supplier_code = "54"
  and sku = "K5-134-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "448"
where supplier_code = "54"
  and sku = "K5-134-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "701"
where supplier_code = "54"
  and sku = "K5-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "394"
where supplier_code = "54"
  and sku = "K5-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "362"
where supplier_code = "54"
  and sku = "K5-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "543"
where supplier_code = "54"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "409"
where supplier_code = "54"
  and sku = "K5-134-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "8"
where supplier_code = "54"
  and sku = "K5-134-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "33"
where supplier_code = "54"
  and sku = "K5-134-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "363"
where supplier_code = "54"
  and sku = "K5-136C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "260"
where supplier_code = "54"
  and sku = "K5-136C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "463"
where supplier_code = "54"
  and sku = "K5-136C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1572"
where supplier_code = "54"
  and sku = "K5-136C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1742"
where supplier_code = "54"
  and sku = "K5-136C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "2147"
where supplier_code = "54"
  and sku = "K5-136C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1201"
where supplier_code = "54"
  and sku = "K5-136C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1268"
where supplier_code = "54"
  and sku = "K5-136C-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "260"
where supplier_code = "54"
  and sku = "K5-360C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "602"
where supplier_code = "54"
  and sku = "K5-360C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "912"
where supplier_code = "54"
  and sku = "K5-360C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "218"
where supplier_code = "54"
  and sku = "K5-360C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1499"
where supplier_code = "54"
  and sku = "K5-360C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1415"
where supplier_code = "54"
  and sku = "K5-360C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "251"
where supplier_code = "54"
  and sku = "K5-360C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1334"
where supplier_code = "54"
  and sku = "K5-360C-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "110",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-44C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "102",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-44-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "145"
where supplier_code = "54"
  and sku = "K5-44-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "118"
where supplier_code = "54"
  and sku = "K5-44-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "29"
where supplier_code = "54"
  and sku = "K5-44-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "300"
where supplier_code = "54"
  and sku = "K5-55C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "747"
where supplier_code = "54"
  and sku = "K5-55C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "709"
where supplier_code = "54"
  and sku = "K5-55C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "932"
where supplier_code = "54"
  and sku = "K5-55C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "637"
where supplier_code = "54"
  and sku = "K5-55C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "41",
    self_provide_inventory = "1273"
where supplier_code = "54"
  and sku = "K5-55C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "9",
    self_provide_inventory = "1049"
where supplier_code = "54"
  and sku = "K5-55C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "56",
    self_provide_inventory = "1184"
where supplier_code = "54"
  and sku = "K5-55C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "799"
where supplier_code = "54"
  and sku = "K5-55C-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "71"
where supplier_code = "54"
  and sku = "K5-55L-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "34"
where supplier_code = "54"
  and sku = "K5-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1273"
where supplier_code = "54"
  and sku = "K5-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "791"
where supplier_code = "54"
  and sku = "K5-55L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "176"
where supplier_code = "54"
  and sku = "K5-55L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "206"
where supplier_code = "54"
  and sku = "K5-55L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "140"
where supplier_code = "54"
  and sku = "K5-55L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "559"
where supplier_code = "54"
  and sku = "K5-55L-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "958"
where supplier_code = "54"
  and sku = "K5-55L-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "310"
where supplier_code = "54"
  and sku = "K5-55-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "366",
    self_provide_inventory = "644"
where supplier_code = "54"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "361",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "169",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "407",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "12",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-55-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "46",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-FR-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "1422",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-FR-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "268"
where supplier_code = "54"
  and sku = "K5-FUC-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "364"
where supplier_code = "54"
  and sku = "K5-FUC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1548"
where supplier_code = "54"
  and sku = "K5-FUC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1911"
where supplier_code = "54"
  and sku = "K5-FUC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1853"
where supplier_code = "54"
  and sku = "K5-FUC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1748"
where supplier_code = "54"
  and sku = "K5-FUC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1336"
where supplier_code = "54"
  and sku = "K5-FUC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1171"
where supplier_code = "54"
  and sku = "K5-FUC-ST-24-F-NC";
update supplier_inventory
set stock_up_inventory     = "96",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-X1-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "851",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-X1-ST-14-M-NC";
update supplier_inventory
set stock_up_inventory     = "143",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-10-C-NC";
update supplier_inventory
set stock_up_inventory     = "151",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "302",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "104",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-12-M-NC";
update supplier_inventory
set stock_up_inventory     = "15",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-14-M-NC";
update supplier_inventory
set stock_up_inventory     = "117",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-18-M-NC";
update supplier_inventory
set stock_up_inventory     = "201",
    self_provide_inventory = "0"
where supplier_code = "54"
  and sku = "K5-XTL-ST-20-M-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "253"
where supplier_code = "69"
  and sku = "K5-134C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "250"
where supplier_code = "69"
  and sku = "K5-134C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "244"
where supplier_code = "69"
  and sku = "K5-134C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "230"
where supplier_code = "69"
  and sku = "K5-134C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "250"
where supplier_code = "69"
  and sku = "K5-134C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "230"
where supplier_code = "69"
  and sku = "K5-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "985"
where supplier_code = "69"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1254"
where supplier_code = "69"
  and sku = "K5-134-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "623"
where supplier_code = "69"
  and sku = "K5-44-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "344"
where supplier_code = "69"
  and sku = "K5-44-ST-10-F-4";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "113"
where supplier_code = "69"
  and sku = "K5-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "118"
where supplier_code = "69"
  and sku = "K5-44-ST-12-F-4";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "579"
where supplier_code = "69"
  and sku = "K5-44-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "529"
where supplier_code = "69"
  and sku = "K5-44-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "307"
where supplier_code = "69"
  and sku = "K7-44L-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "607",
    self_provide_inventory = "0"
where supplier_code = "69"
  and sku = "K7-44-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "1370",
    self_provide_inventory = "0"
where supplier_code = "69"
  and sku = "K7-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "28",
    self_provide_inventory = "0"
where supplier_code = "69"
  and sku = "K7-44-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "274"
where supplier_code = "75"
  and sku = "K5-134C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "618"
where supplier_code = "75"
  and sku = "K5-134C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "406"
where supplier_code = "75"
  and sku = "K5-134C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "159"
where supplier_code = "75"
  and sku = "K5-134C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1"
where supplier_code = "75"
  and sku = "K5-44C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "549"
where supplier_code = "75"
  and sku = "K5-44C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "292"
where supplier_code = "75"
  and sku = "K5-44C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1792"
where supplier_code = "75"
  and sku = "K5-44C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1467"
where supplier_code = "75"
  and sku = "K5-44C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "2164"
where supplier_code = "75"
  and sku = "K5-44C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "3208"
where supplier_code = "75"
  and sku = "K5-44C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "636"
where supplier_code = "75"
  and sku = "K5-44C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "845"
where supplier_code = "75"
  and sku = "K5-55C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "525"
where supplier_code = "75"
  and sku = "K5-55C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "369"
where supplier_code = "75"
  and sku = "K5-55C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "690"
where supplier_code = "75"
  and sku = "K5-55C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "427"
where supplier_code = "75"
  and sku = "K5-55C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "1300"
where supplier_code = "75"
  and sku = "K5-55C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "0",
    self_provide_inventory = "2051"
where supplier_code = "75"
  and sku = "K5-55C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "480",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "210",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "720",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "480",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "700",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "1000",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "1500",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "1200",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "300",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-134-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "400",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-136-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "110",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-136-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-136-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "120",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-360-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-360-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "70",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-360-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "600",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "500",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "300",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "900",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "1000",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "1500",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "80",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "280",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "2700",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "2700",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "1660",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "2300",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "700",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-44-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "150",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "120",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "500",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "500",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "980",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "60",
    self_provide_inventory = "0"
where supplier_code = "76"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "150",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-134L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "27",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-134L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "53",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-134L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "745",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-134L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "253",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "40",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44C-ST-10-A33";
update supplier_inventory
set stock_up_inventory     = "1252",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "730",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44C-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "113",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "24",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "170",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-44-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "408",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-06-F-NC";
update supplier_inventory
set stock_up_inventory     = "906",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "700",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "1600",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "1000",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "2600",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "930",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "260",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55C-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "280",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-08-F-NC";
update supplier_inventory
set stock_up_inventory     = "18",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "748",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "1640",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "332",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "380",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "196",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55LC-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "870",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "6193",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "4317",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "3845",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "2705",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "3279",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55L-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "235",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "2599",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "746",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "20",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-16-A33";
update supplier_inventory
set stock_up_inventory     = "727",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "272",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "262",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-55-ST-22-A33";
update supplier_inventory
set stock_up_inventory     = "99",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-10-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "309",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-12-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "389",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-14-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "202",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-16-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "298",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-18-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "121",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-76C-ST-20-F-NC-JBLK";
update supplier_inventory
set stock_up_inventory     = "111",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-DYLC-ST-22-F-NC";
update supplier_inventory
set stock_up_inventory     = "56",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-KTC-ST-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "49",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-KTC-ST-18-M-NC";
update supplier_inventory
set stock_up_inventory     = "49",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-KTC-ST-20-M-NC";
update supplier_inventory
set stock_up_inventory     = "19",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-X1C-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "15",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-X1C-ST-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "648",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-X1C-ST-20-M-NC";
update supplier_inventory
set stock_up_inventory     = "4497",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-10-C-NC-左";
update supplier_inventory
set stock_up_inventory     = "150",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-12-C-NC-右";
update supplier_inventory
set stock_up_inventory     = "938",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-12-C-NC-左";
update supplier_inventory
set stock_up_inventory     = "11",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-14-C-NC-右";
update supplier_inventory
set stock_up_inventory     = "32",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-16-C-NC-右";
update supplier_inventory
set stock_up_inventory     = "119",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-16-M-NC";
update supplier_inventory
set stock_up_inventory     = "300",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XTLC-ST-20-M-NC";
update supplier_inventory
set stock_up_inventory     = "187",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XYLC-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "238",
    self_provide_inventory = "0"
where supplier_code = "79"
  and sku = "K5-XYLC-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "3000",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "1548",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-134-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "702",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-134-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "634",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-134-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "490",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "139",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-14-C-M22730";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-14-F-AH";
update supplier_inventory
set stock_up_inventory     = "996",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "28",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-14-F-NH";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-16-F-AH";
update supplier_inventory
set stock_up_inventory     = "1681",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-16-F-NC";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-16-F-NH";
update supplier_inventory
set stock_up_inventory     = "155",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-18-C-M22730";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-18-F-AH";
update supplier_inventory
set stock_up_inventory     = "1081",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-18-F-NC";
update supplier_inventory
set stock_up_inventory     = "100",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-18-F-NH";
update supplier_inventory
set stock_up_inventory     = "50",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-20-F-AH";
update supplier_inventory
set stock_up_inventory     = "279",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-55-ST-20-F-NC";
update supplier_inventory
set stock_up_inventory     = "570",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-X1-WW-06-M-AH";
update supplier_inventory
set stock_up_inventory     = "1018",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-X1-WW-08-M-NC";
update supplier_inventory
set stock_up_inventory     = "2210",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-XTL-ST-08-S-NC";
update supplier_inventory
set stock_up_inventory     = "752",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-XTL-ST-10-C-NC";
update supplier_inventory
set stock_up_inventory     = "382",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-XTL-ST-12-C-NC";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K5-XTL-ST-16-C-NC";
update supplier_inventory
set stock_up_inventory     = "1457",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-134-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "1173",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-134-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "417",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-44-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "150",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-44-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "609",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-55L-ST-10-F-NC";
update supplier_inventory
set stock_up_inventory     = "438",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-55L-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "682",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-55-ST-14-F-NC-PBLK";
update supplier_inventory
set stock_up_inventory     = "200",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-KT-ST-10-M-NC";
update supplier_inventory
set stock_up_inventory     = "246",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-X1-ST-10-M-M460";
update supplier_inventory
set stock_up_inventory     = "41",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-XTL-ST-12-C-H6";
update supplier_inventory
set stock_up_inventory     = "599",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-XTL-ST-12-F-NC";
update supplier_inventory
set stock_up_inventory     = "67",
    self_provide_inventory = "0"
where supplier_code = "96"
  and sku = "K7-XTL-ST-14-F-NC";
update supplier_inventory
set stock_up_inventory     = "2212",
    self_provide_inventory = "0"
where supplier_code = "119"
  and sku = "K5-55-PIX-06-S-NC";