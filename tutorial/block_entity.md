
# 第一步：创建方块实体

为了创造我们的机器，首先必须创造其方块。

在这里姑且命名它为 ExampleMachineBlock 。

![image](https://github.com/user-attachments/assets/e6180119-51d6-4dbb-a32d-7ed3332dde8a)

这里IDE会报错，因为我们需要实现一个方法：newEntity

![image](https://github.com/user-attachments/assets/e7d2fdb6-9240-494b-ab7d-0489608348ac)

但是我们还没创建方块实体，因此先让它返回null，创建好构造器之后就可以照常注册了

![image](https://github.com/user-attachments/assets/0eff7f79-8ce4-4d4d-bc0b-71881dcb6341)

现在我们来创建方块实体：

![image](https://github.com/user-attachments/assets/436e5c90-1aaa-4d24-b5da-a86fda2bfa90)

记得在IDE自动生成构造器之后把第一个参数删掉，super里的第一个参数留空

我们需要先注册对应的EntityType：

![image](https://github.com/user-attachments/assets/8c7d3ac3-c476-470e-b176-dd4e6fdbe139)

（是的，我知道这看上去和其他的注册有些不同，但是暂且先适应一下吧）

现在可以回到方块实体的构造器那里：

![image](https://github.com/user-attachments/assets/aeaf8c66-af86-4caa-a225-b207890ea1b4)

还记得Block类里的那个方法吗？现在我们有东西给他返回了：

![image](https://github.com/user-attachments/assets/1ac2e2df-4498-4580-a242-b1faa2d2986c)

别忘了注册对应的BlockItem：

![image](https://github.com/user-attachments/assets/5e62d045-7b40-4e36-a65d-94cb195e40a5)

我们刚刚创建了一个带有方块实体的方块！让我们进游戏看看效果：

![image](https://github.com/user-attachments/assets/975a1fdd-8bc8-4cc1-b990-375c99779229)

额。。。看上去不是非常有意思。这个方块完全没有模型！更糟的是，它把下面方块的材质也剔除掉了。不过至少Minecraft的/data指令能正确的将其识别为方块实体了。
