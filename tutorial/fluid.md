
# 第五步：流啊流（IFluidHandler）

是的，作者是说过本篇教程会注重于IItemHandler。请当作者没说过。

现在我们的机器能够处理物品配方了。遗憾的是，物品已经跟不上时代了 —— Forge早已经是流体和能量的天下了。

为了跟上时代的步伐，我们现在给机器加上IFluidHandler，让它能处理流体相关的配方。

先说坏消息：这次没有捷径可以走了，我们只能自己手动实现IFluidHandler（和INBTSerializable）。

由于此接口的方法实在有些多而且很无聊，故不在教程内部展示截图，可以[点击链接](/src/main/java/dev/fiberoptics/machineexample/capability/ModFluidStackHandler.java)自行查看。

我们为机器设计了一个两格物品栏和一个两桶的流体栏。每个栏有输入位和输出位各一。

因此要对ModItemStackHandler进行一些修改：

![image](https://github.com/user-attachments/assets/ede5dedb-5ec3-474c-a79e-1775e94f52d6)

（这玩意看上去比隔壁FluidHandler真是顺眼多了）

让我们给ExampleMachineBlockEntity类加上我们的流体储存：

![image](https://github.com/user-attachments/assets/de7413a4-9cea-485e-90cf-fefc3d3c55d7)

记得也加上流体储存的nbt读写，不然游戏可能要崩（别问我怎么知道的）：

![image](https://github.com/user-attachments/assets/3a128fd9-6cc6-4813-8ec2-b90ede264bcd)

下面进游戏看看效果。我们用机动的泵和创造储罐向机器输入岩浆：

![image](https://github.com/user-attachments/assets/e3a2fa96-4d76-4e55-be60-b6e96b9cbe32)

嗯，我们的机器已经可以接受流体输入了。现在让我们看看如何把流体加进配方。

不幸的是，修改前文中的配方格式是一个较为繁琐的过程，很难在文中进行详细说明。作者在这里列举一些比较重要的点：

  · 将SimpleContainer替换为继承的ModContainer，以支持在配方中搜索流体。
  · 改变Serializer的格式，从而读取流体输入和输出。
  · 改变机器方块实体的tick逻辑，使其同时监管四个I/O槽位。

对具体修改过程有兴趣的读者可以移步commit详情：https://github.com/fiberoptics-0/MachineExample/commit/eb1b92c55cecc35c64f46c3659c28fecace52617 自行查看。

现在我们可以添加测试配方：

![image](https://github.com/user-attachments/assets/32c7621a-940f-429d-9be6-0cf69d95f644)

这是一个简单的矿物处理配方，消耗1000mB岩浆将1粗铁转化为2铁锭，并产生1000mB水。

让我们进入游戏，向机器添加一个粗铁：

![image](https://github.com/user-attachments/assets/6f04c0e5-8960-46a4-96cc-46440ff7d5ba)
