# 基于JavaSE实现的高并发选课系统



## 第一部分-项目简介

该选课系统基于Java实现，以文件为数据存储的底层支持，能够较好地并发处理多线程请求，同时引入LRU缓存使其具有较好的性能。

项目以服务端-客户端架构为基础，实现了一个服务端，以及两个客户端—学生客户端和教务/管理员客户端。其流程图如下：

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/1.png)

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/2.png)

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/3.png)



## 第二部分-总体设计

### 1，模块设计

项目整体上分为三大模块：文件模块，缓存模块，网络模块（又分为客户端，服务端）。文件模块提供最底层的IO功能；缓存模块在解决好置换算法的基础上对文件功能进行了进一步的封装；网络模块处理网络连接，同时依赖缓存进行各种操作，支持多线程。

### 2，类的设计

基础类：Student(学生对象), Course(课程对象), SelectInfo(选课信息)

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/4.png)

接口：Cacheable, ClientView, ManagerClientView, StudentClientView, ManagerStrategy, 

StudentStrategy, ServerStrategy

缓存：LRUCache, StudentCache

文件：CourseFile, StudentInfoFile, SelectInfoFile

网络： ServerProperty,  ServerThread,  ThreadPoolSupport, 

ServerStrategyImpl,  CourseServer,  ClientProperty,

ManagerClient,  ManagerStrategyImpl,  ManagerWindow

StudentClient,  StudentStrategyImpl,  StudentWindow

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/5.png)

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/6.png)

### 3，关于缓存模块的实现

在该项目中，我负责缓存模块的实现。详细设计如下：

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/7.png)

基于可复用的考虑，我先实现了一个带有泛型参数的LRUCache<K, V>，具有基本的LRU置换功能，但它本身仍是抽象类。落实到具体的应用场景也就是这个选课系统，继承LRUCache<Student, CoursePage>实现了StudentCache。（说明：CoursePage封装了对应学生的选课信息数组）

StudentCache缓存的每一条记录为<Student, CoursePage>元组，设置缓存的最大容量(capacity)，实现对学生和选课记录的部分缓存。同时，StudentCache含有List对象courseList实现对少量课程信息的全部缓存。

 缓存的预加载(preLoad)：加载学号从1到capacity+1的学生的<Student, CoursePage>元组。

缓存的置换：使用LRU(最近最少使用)置换算法，复写了父类LinkedHashMap的removeEldestEntry()，在将最久未访问的记录移除缓存之前，根据CoursePage封装的修改位判断该最“老”记录是否被修改，若被修改则需要调用文件模块的方法将被修改的记录写回文件，，若未被修改过，则直接将该记录移除缓存。

缓存缺失的处理：若服务端要求访问一个当前缓存中不存在的记录，那么此时就称发生了缓存的缺失。这时，将从文件中查找要访问的学生对象是否存在，若存在则调入缓存，不存在则提示不存在。若调入缓存时，缓存已满则将使用LRU置换策略，同时若被修改则会先写回文件再移除缓存。

 缓存的关闭：在服务端关闭或维护之前，将关闭缓存。关闭缓存操作将遍历当前缓存的所有记录，若修改位为true则要写回文件。

同时，缓存封装了各种文件操作，供服务端直接调用而不需要服务端显示进行文件操作。

例如addCourse(), removeCourse()等方法。这些方法因为内部需要修改共同的资源(如课余量，选课人数，学生对象等)往往是加锁(synchronized)进行了同步。

**缓存模块实现的自我评价**：

缓存的存储，我们最开始思考的是用三个List来实现，但仔细考虑到学生，课程，选课信息的关系，我们决定用一种类似“省市联动”的思想，以<学生，选课信息>为元组进行缓存，注意到，选课信息是一个数组并且每个数组仅存与某个学生有关的选课信息。进而再将选课信息数组与一条记录的修改位进行封装，封装成CoursePage。最终形成以<Student, CoursePage>为记录的缓存。

缓存的置换算法，实现基于LinkedHashMap，既有天然的优势，也有天然的劣势。优势是便于实现LRU的功能，而劣势是因为LinkedHashMap使用散列表+双向链表的存储结构以访问顺序构造，导致仅仅是访问一条记录都会修改内部结构，在多线程并发是会发生一些奇怪的问题。暴力的解决方式是访问和修改都加了锁，这样免不了性能的降低。

但综合考虑后，仍然决定用这种方式实现缓存。LRU基于程序的局部性原理，能够很大程度地提高缓存的命中率，而文件的按顺序存储以及利用RandomAccessFile的seek方法，极大地提高了文件中查找，以及缓存的置换效率，一定程度上“中和”了同步访问修改的性能损失，并且最终的测试也表明，这样实现的整体性能仍然是不错的。



## 第四部分-系统测试

测试用例表如下：

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/8.png)

![image text](https://github.com/AlieZ22/CourseSystem/blob/master/res/9.png)

