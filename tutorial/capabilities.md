
# 第二步：物品I/O（Capabilities）

请忽略标题里的冷笑话。

为了适应各种物流mod层出不穷，物品/流体/能量鱼龙混杂的时代，Forge推出了Capabilities来代替掉老旧的Container和WorldlyContainer。

Capabilities随着方块的面而变化。例如，一个机器可能在顶部和底部运输物品，南面和北面运输流体，东面和西面运输能量。

Forge本体支持三种Capabilities：IItemHandler（物品），IFluidHandler（流体）和 IEnergyHandler（能量）。本教程将着重于IItemHandler的使用。

想要使用IItemHandler这个接口，我们得先手动实现其方法——————

![image](https://github.com/user-attachments/assets/90e8d19c-44fd-4478-be5a-99277178bb27)

——————嗯，我看还是算了。

万幸的是，Forge为我们提供了一个已经实现好的类：ItemStackHandler。这个类可以模拟一个大小为n的物品存储。

为了让游戏知道我们的方块实体有对应的Capabilities，我们需要继承CapabilityProvider。然而这个类已经被BlockEntity继承了，所以我们可以直接重写：

![image](https://github.com/user-attachments/assets/e2367e92-4e0a-47e5-9fdf-c3548b5f754d)

这个机器可以在顶部，底部和西侧进行物品I/O。

你可能会疑惑LazyOptional的意义。本质上这个类就是Forge类Lazy和Java原生类Optional的结合。

Lazy -- 其对应的值只有在被需要的时候才会进行获取/计算。

Optional -- 其对应的值可能为空。

现在这个机器已经可以被当作1格容量的没有ui的箱子用了。但是我们可不想在摧毁机器的时候丢失全部物品。因此我们可以定义如下方法：

![image](https://github.com/user-attachments/assets/4483f73c-4338-4e8c-845d-a0338b55cb20)

（其实对于一格的物品栏完全可以手动生成掉落物，作者这里偷了个懒用了通用的方法。）

然后在ExampleMachineBlock里重写：

![image](https://github.com/user-attachments/assets/b6c64ef3-e7ab-4bce-ac3b-3ecd657328de)

现在该机器会在被破坏的时候掉落内容物了。

让我们进入游戏看看效果：

![image](https://github.com/user-attachments/assets/09b8daf9-1bd2-4559-8b8f-e3689bfbc010)

我们向漏斗中依次放入半组铁锭，半组红石粉，一组铁锭：

![image](https://github.com/user-attachments/assets/5347ad09-0030-4a38-aa27-c124cd406eb3)

可以看到，机器只能接收一格的物品。（此时若破坏机器则会掉落一组铁锭）

我们移除顶部的漏斗，在底部放置一漏斗：

![image](https://github.com/user-attachments/assets/9af6afe5-4e9b-438b-9cf1-e5aa598158da)

可以看到底部的物品I/O也是正常的。

那么侧面的物流如何呢？如图所示：

![image](https://github.com/user-attachments/assets/c5d7c791-1aaa-428f-b9a2-fe94105f4f72)

此时只有西面的漏斗的内容物会被传输。

现在我们移除漏斗，安装四个安山漏斗：

![image](https://github.com/user-attachments/assets/eebf46b7-3fb7-472a-b0a1-a4a5703f017a)

如图所示，只有西面的安山漏斗会输出物品。

虽然这个机器看上去还不错，但是我们很快会发现一个问题：如果我们退出世界再重新加载世界，内容物会全部消失。

这是因为游戏并不会自动帮你保存方块实体的所有数据 - 你必须手动提供一个nbt作为你想要保存的数据。

把一整个物品存储变成nbt肯定是件苦差事，但是！ItemStackHandler 已经非常贴心的为我们实现好了 INBTSerializable（Forge，你好温油）

因此我们可以直接重写：

![image](https://github.com/user-attachments/assets/df349b8d-e43e-4b84-941d-2000605d8077)

现在机器会在重新加载的时候保留内容物了。

![image](https://github.com/user-attachments/assets/d010ff9d-f1df-406c-a3ed-64590022f7c2)

（不仅如此，现在你还能用/data指令看到机器的内容物了）

[**点此进入下一章节**](/tutorial/tick.md)
