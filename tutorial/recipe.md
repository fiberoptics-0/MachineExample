
# 第四步：蟹老板的秘密配方（Recipe）

虽然我们现在有了一个能处理物品的机器，但是把配方藏在代码里恐怕不是什么明智之举：这样很难维护，并且每个想要调整机器配方的人都会有对我们说出惊世之语的冲动。

相反，我们更希望通过外置的数据包（或者是KJS之类的玩意儿）来决定机器的配方。

让我们首先实现一个Recipe接口：

![image](https://github.com/user-attachments/assets/0dee05f7-801f-4084-9f73-d056488684ae)

IDE会报错，因为我们尚未实现该接口的方法。让我们看看都有哪些方法——————

![image](https://github.com/user-attachments/assets/d97f4d6d-61ab-4c13-9d7d-d1128789bdb1)

——————怀疑人生.jpg

唉，我们一个一个来吧。

首先，我们得声明一些必须的常量和对应的构造器：

![image](https://github.com/user-attachments/assets/88cb70e8-268d-4bf8-8be9-ea1276f58241)

现在让我们来看看这些方法。

![image](https://github.com/user-attachments/assets/657ab97d-1af8-47a5-88d7-ee7a3dbdc591)

matches会根据一组特定的输入检测是否与当前配方的ingredients所匹配。由于我们并不关心原材料的顺序，因此我们必须考虑所有的情况。

![image](https://github.com/user-attachments/assets/75dac711-5be5-43fc-8663-613bf2bc7586)

assemble会根据一组特定的输入输出一个结果。我们并不是很关心这个，直接输出result的copy就可以了。

![image](https://github.com/user-attachments/assets/430a7764-f465-40e7-8a1f-40e95415431b)

canCraftInDimensions主要被用于筛选工作台合成配方。例如：铁剑无法在2x2的网格内合成，因此在筛选配方时会被直接排除。由于我们并不是合成台配方，可以无脑返回true。

![image](https://github.com/user-attachments/assets/cd1f07ab-7db7-4fb3-bab5-13f2ce8f8734)

getResultItem，简明易懂，获得产出物品。在有些情况下（比如配方有多个输出）甚至用不到这个方法，但是我们正好只有一个输出，直接返回result的copy。

![image](https://github.com/user-attachments/assets/40678601-710e-48cc-9586-365568409c6e)

这两个方法暂且返回null。

接下来我们需要定义两个类：RecipeType和RecipeSerializer：

![image](https://github.com/user-attachments/assets/4b270ee3-7046-40f5-9947-bb093df64343)

RecipeType非常简单：只有一个static常量返回一个实例。

而RecipeSerializer则又要实现三个方法：

![image](https://github.com/user-attachments/assets/16289800-c5e4-417a-a2af-b6e70529ad08)

我们还是一个一个来。

![image](https://github.com/user-attachments/assets/d84454be-3a61-40cf-88df-aa477ba5de31)

首先是和RecipeType里一致的实例常量。

fromJson实现了游戏是如何从JSON文件中生成Recipe实例的。注意这里读取的写法决定了JSON文件的写法。例如，ingredients读取的是Array，意味着JSON文件的ingredients可以写数组。而output读取的是Element，因此不能写数组。

如果JSON文件的格式与读取方式不符，配方不会成功生成。

![image](https://github.com/user-attachments/assets/187c13d9-5b9f-43ea-a86a-71a739649377)

toNetwork实现了配方在网络通信传输的方式。在这里可以随意选择编码的方式，只要满足两点：所有必要的信息都被编码，以及和fromNetwork的读取顺序保持一致。

![image](https://github.com/user-attachments/assets/9364640f-1afe-402c-bc7d-3c88b7caace4)

fromNetwork其实就是将toNetwork里编码的数据读取出来，但是请务必保证顺序一致，否则不会正常传输。

现在我们完成了RecipeType和RecipeSerializer，可以回到原来的两个方法：

![image](https://github.com/user-attachments/assets/698335e5-6959-43f4-a10d-bf4e4690a026)
