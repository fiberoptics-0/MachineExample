
# 第四步：蟹老板的秘密配方（Recipe）

虽然我们现在有了一个能处理物品的机器，但是把配方藏在代码里恐怕不是什么明智之举：这样很难维护，并且每个想要调整机器配方的人都会有对我们说出惊世之语的冲动。

相反，我们更希望通过外置的数据包（或者是KJS之类的玩意儿）来决定机器的配方。

让我们首先实现一个Recipe接口：

![image](https://github.com/user-attachments/assets/0dee05f7-801f-4084-9f73-d056488684ae)

IDE会报错，因为我们尚未实现该接口的方法。让我们看看都有哪些方法——————

![image](https://github.com/user-attachments/assets/d97f4d6d-61ab-4c13-9d7d-d1128789bdb1)

——————怀疑人生.jpg

唉，我们一个一个来吧。

