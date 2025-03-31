
# 第二步：想要I/O，你有这个“能力”吗？（Capabilities）

请忽略标题里的冷笑话。

为了适应各种物流mod层出不穷，物品/流体/能量鱼龙混杂的时代，Forge推出了Capabilities来代替掉老旧的Container和WorldlyContainer。

Capabilities随着方块的面而变化。例如，一个机器可能在顶部和底部运输物品，南面和北面运输流体，东面和西面运输能量。

Forge本体支持三种Capabilities：IItemHandler（物品），IFluidHandler（流体）和 IEnergyHandler（能量）。本教程将着重于IItemHandler的使用。

想要使用IItemHandler这个接口，我们得先手动实现其方法——————

![image](https://github.com/user-attachments/assets/90e8d19c-44fd-4478-be5a-99277178bb27)

——————嗯，我看还是算了。

万幸的是，Forge为我们提供了一个已经实现好的类：ItemStackHandler。这个类可以模拟一个大小为n的物品存储。

为了让游戏知道我们的方块实体有对应的Capabilities，我们需要实现一个CapabilityProvider<T>接口。
