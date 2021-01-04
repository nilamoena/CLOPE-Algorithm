# CLOPE-Algorithm
Final Project - PKB113

## Group D - Ilmu Komputer 2018 
* Afny (1313618001)
* Nilatil Moena (1313618002)

## Reference Paper

* AUTHOR, "Yiling Yang, Xudong Guan, and Jinyuan You"
* TITLE, "CLOPE: A Fast and Effective Clustering Algorithm for Transactional Data"
* BOOKTITLE, "Proceedings of the eighth ACM SIGKDD international conference on Knowledge discovery and data mining"
* YEAR, "2002"
* PAGES, "682-687"
* PUBLISHER, "ACM  New York, NY, USA"
* [PDF Source](https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.13.7142&rep=rep1&type=pdf)

## Path - Modified Source
* [Original Referenced Paper](https://github.com/nilamoena/CLOPE-Algorithm/blob/master/Paper%20CLOPE.pdf)
* [Java Source Code](https://github.com/nilamoena/CLOPE-Algorithm/blob/master/weka/src/main/java/weka/clusterers/CLOPE.java) CLOPE-Algoritma/weka/src/utama/Jawa/weka/clusterer/ CLOPE.java
* [Compiled Weka jar](https://github.com/nilamoena/CLOPE-Algorithm/tree/master/weka/dist) CLOPE-Algoritma/weka/ dist/weka.jar

## More about CLOPE
Ide dasar di balik CLOPE yaitu menggunakan fungsi kriteria global yang mencoba untuk meningkatkan overlapping intra-cluster dari item transaksi dengan meningkatkan rasio tinggi-lebar histogram cluster dan sebuah parameter untuk mengontrol ketatnya cluster, jumlah cluster yang berbeda dapat diperoleh dengan memvariasikan parameter ini.

### we tried with some data on weka
1. breast-cancer.arff
2. contact-lenses.arff
3. soybean.arff
4. supermarket.arff
5. vote.arff
6. weather.nominal.arff

### repulsion
Generalized with a repulsion parameter that controls tightness of transactions in a cluster, and thus the resulting number of clusters
* repulsion default = 2.5
