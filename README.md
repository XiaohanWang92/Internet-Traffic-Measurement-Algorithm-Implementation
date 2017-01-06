# Internet-Traffic-Measurement-Algorithm-Implemnetation
Implement 3 popular and powerful traffic measurement algorithms.

This is a school course project.

Teammate: [Yunze Li](https://www.linkedin.com/in/yunze-li-b8a86a112)

Data Source: [Data](http://www.cise.ufl.edu/~sgchen/FlowTraffic.zip)

Algorithm References:

* Linear Counting Algorithm: Whang, Kyu-Young, Brad T. Vander-Zanden, and Howard M. Taylor. "A linear-time probabilistic counting algorithm for database applications." ACM Transactions on Database Systems (TODS) 15.2 (1990): 208-229.

* FMSketch: Flajolet, Philippe, and G. Nigel Martin. "Probabilistic counting." Foundations of Computer Science, 1983., 24th Annual Symposium on. IEEE, 1983.

* Virtual Bit Map: Estan, Cristian, George Varghese, and Mike Fisk. "Bitmap algorithms for counting active flows on high speed links." Proceedings of the 3rd ACM SIGCOMM conference on Internet measurement. ACM, 2003.

Possible Problem:
To hash each element in the flow, we use Java hashcode method. Sometimes this method (for Integer and String) will return a negative value. The right implementation should not absolute the negative value into positive value. The proper way is using a base as an addition to make all possible negative values positive. For example, if the hashcode range is [-100, 100], we can add 101 on this range to make it becomes [1, 201] so every hashcode value is positive.

Result:

* Linear Counting Algorithm:
![alt text][LCA]
[LCA]: https://raw.githubusercontent.com/XiaohanWang92/Internet-Traffic-Measurement-Algorithm-Implemnetation/master/result/LinearCountingResult.png

* FM:
![alt text][FM]
[FM]: https://raw.githubusercontent.com/XiaohanWang92/Internet-Traffic-Measurement-Algorithm-Implemnetation/master/result/FMSketch.png

* VirtualBitMap:
![alt text][virtualBM]
[virtualBM]: https://raw.githubusercontent.com/XiaohanWang92/Internet-Traffic-Measurement-Algorithm-Implemnetation/master/result/VirtualBitMapResult.png
