# CLOPE-Algorithm
Final Project - PKB113

## Group D - Ilmu Komputer 2018 
* Afny (1313618001)
* Nilatil Moena (1313618002)

## Reference Paper
* Yiling Yang, Xudong Guan, and Jinyuan You.2002."CLOPE: A Fast and Effective Clustering Algorithm for Transactional Data" in: Proc of KDD'02(pages 682-687). Departement of Computer Science & Engineering, Shanghai Jiao Tong University. ACM  New York, NY, USA.
* [Source](https://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.13.7142)
* [Original Referenced Paper](https://github.com/nilamoena/CLOPE-Algorithm/blob/master/Paper%20CLOPE.pdf)

## Path - Modified Source
* [Java Source Code](https://github.com/nilamoena/CLOPE-Algorithm/blob/master/weka/src/main/java/weka/clusterers/CLOPE.java) weka/ src/ main/ java/ weka/ clusterers/CLOPE.java
* [Compiled Weka jar](https://github.com/nilamoena/CLOPE-Algorithm/tree/master/weka/dist) weka/ dist/weka.jar

## More about CLOPE
Algoritma clustering CLOPE yaitu menggunakan fungsi kriteria global yang mencoba untuk meningkatkan overlapping intra-cluster dari item transaksi dengan meningkatkan rasio tinggi-lebar histogram cluster dan sebuah parameter untuk mengontrol ketatnya cluster, jumlah cluster yang berbeda dapat diperoleh dengan memvariasikan parameter ini.

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
