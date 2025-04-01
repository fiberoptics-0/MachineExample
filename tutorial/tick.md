
# 第三步：嘀嗒，嘀嗒（tick）

现在我们有了一个最基本的容器，但是这个甚至不如原版箱子的东西肯定不是我们想要的。

要为机器添加处理物品的功能，我们必须处理一个新问题：输入和输出槽的I/O区分。在目前使用的ItemStackHandler下，外部物流可以无限制地从所有格子里抽取或放入物品，这意味着可能会把输入槽里的物品抽走或是把原料塞进输出槽里。

本着“能偷懒就偷懒”的理念，我们直接继承ItemStackHandler类，把这两种错误的物流方式堵死：

![image](https://github.com/user-attachments/assets/119a3a93-34f0-4aae-8edc-a151f5178498)

这里我们定义了一个三格的物品栏，并规定0号，1号槽位为输入，2号槽位为输出。（这些数字要记住，之后要用）

然后在ExampleMachineBlockEntity里替换掉相应属性：

![image](https://github.com/user-attachments/assets/7ce1e679-245e-431c-9de5-22fbb50de837)

并修改getCapability使物流扩展到所有六个面上(非必需)：

![image](https://github.com/user-attachments/assets/6e9b23c2-5763-4203-b40b-821c3c51d5b6)

我们现在成功隔离开输入和输出了，但是机器还没有开始处理物品。我们首先定义当前进度和最大进度：

![image](https://github.com/user-attachments/assets/5e1397d1-9083-48e0-9b44-00669bd4feb9)

现在我们试图添加一个硬编码的“配方”：一个铁锭加一个金粒合成一个钻石。

我们首先书写tick逻辑：在所有原材料都满足的时候增加进度，进度满后扣除原材料各一并生成输出。

![image](https://github.com/user-attachments/assets/212ea228-eea7-41b8-8ecf-8ead7980fa71)

（其实生成输出这一步本来可以用ItemStackHandler的自带方法判断的，可惜我们亲手把那个方法堵死了，嘻嘻）

然后是ticker助手方法：

![image](https://github.com/user-attachments/assets/5e55fdcc-65ae-4973-9f39-372cea599cda)

我们需要告诉游戏去应用这个tick逻辑。在ExampleMachineBlock类里添加如下重写：

![image](https://github.com/user-attachments/assets/7cf2f7e5-2d79-4433-b966-8cb704e3589f)

（是的，你没看错，那个助手方法只是为了让作者能少写一个lambda）

最后，别忘了在nbt存取中添加进度值，让机器能在重新加载的时候保留进度。

![image](https://github.com/user-attachments/assets/3ad4d987-d1b2-422e-9c4a-a7b0356872de)

现在我们的机器可以开始处理物品了！让我们进游戏看看效果：

![image](https://github.com/user-attachments/assets/143352c1-9e24-4340-84bc-4137a3f5026e)

经过测试可以发现输入物流只能访问前两个槽，输出物流只能访问第三个。

让我们依次放入铁锭和金粒：

（说句题外话，由于这个奇葩的硬编码方式，反过来放 —— 也就是先放金粒再放铁锭 —— 是不行的）

![image](https://github.com/user-attachments/assets/a26c5e3a-4ac3-4a55-ab1b-f68d3475267c)

很好，现在我们的机器终于在某些方面上超越了原版方块 —— 至少比熔炉强一丁点吧。
