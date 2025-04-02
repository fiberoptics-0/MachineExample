
# 第五步：流啊流（IFluidHandler）

是的，作者是说过本篇教程会注重于IItemHandler。请当作者没说过。

现在我们的机器能够处理物品配方了。遗憾的是，物品已经跟不上时代了 —— Forge早已经是流体和能量的天下了。

为了跟上时代的步伐，我们现在给机器加上IFluidHandler，让它能处理流体相关的配方。

先说坏消息：这次没有捷径可以走了，我们只能自己手动实现IFluidHandler（和INBTSerializable）：

![image](https://github.com/user-attachments/assets/36bc70c0-98e0-45ce-943f-7c477e832d61)

唉，我们还是一个一个来。

![image](https://github.com/user-attachments/assets/6fed4f8f-eb1b-4257-85d7-77ef24ea140d)

首先是必要的流体列表（用来存储流体）以及我们自己要用到的设置流体的方法。

![image](https://github.com/user-attachments/assets/61ec83ac-9b03-4e08-aa49-e2446b928bcb)

getTanks会返回流体存储的格数。

![image](https://github.com/user-attachments/assets/ba66cc6b-6049-42c1-8571-e09899c2dade)

getFluidInTank会返回特定格子里的流体。

![image](https://github.com/user-attachments/assets/a2ad6cb1-6991-4e5a-9196-f0b08dfd2319)

getTankCapacity会返回特定格子的容量，我们这里直接设置为10000。

![image](https://github.com/user-attachments/assets/7fec6022-bac3-4d35-892d-208afe0d6636)

isFluidValid我们现在还用不上，直接无脑true。

![image](https://github.com/user-attachments/assets/930fcb21-3124-4c45-a697-fe2709cede0e)

fill实现了对流体容器的输入。注意我们这里只允许第一格槽位接受输入。

![image](https://github.com/user-attachments/assets/38fa601c-f4f0-4caf-aebe-79919f0b55d6)

drain方法实现了流体的抽取。我们这里只允许第二个槽位输出。

![image](https://github.com/user-attachments/assets/00e08c7b-93b9-4180-bfcb-654b6cf22a9c)

这个drain方法实现了特定流体的抽取，我们可以直接调用上一个drain方法。

![image](https://github.com/user-attachments/assets/ad79e09c-ee85-4cc1-ad52-e7ea40ca4495)

serializeNBT将流体存储的信息存到nbt数据内。可以随意决定存储方式，只要在读取的时候保持一致就可以。作者这里直接从ItemStackHandler里抄了一份并做了些微调。

![image](https://github.com/user-attachments/assets/863e1e0f-b83c-40b2-8967-876f9f3f76a5)

deserializeNBT从nbt数据内读取流体存储信息。请务必和serializeNBT中的格式保持一致。

我们打算为机器设计一个两格物品栏和一个两桶的流体栏。每个栏有输入位和输出位各一。

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

第一步是继承一个SimpleContainer并加入流体逻辑，用以进行配方匹配。

![image](https://github.com/user-attachments/assets/87ae3c48-7486-407d-9883-0b44e2722484)

然后修改配方类的继承：

![image](https://github.com/user-attachments/assets/c0c75ca8-0f1f-48ac-943a-e5d93620e5cf)

现在改变ExampleMachineRecipe的常量和构造器：

![image](https://github.com/user-attachments/assets/f36dda59-31f5-4332-8de3-3055c67ce25b)

更改matches方法：

![image](https://github.com/user-attachments/assets/6e3f6326-de0f-46d3-9268-963063342a70)

并添加一个我们自己要用到的获取流体消耗量的方法：

![image](https://github.com/user-attachments/assets/901bf211-f7e9-4161-9ea6-cd7cac69441a)

配方里的其他几个方法可以暂时不用动，反正也用不着。

然后我们需要实现配方的json读取逻辑。

遗憾的是，作者没找到FluidStack的内置转化json方法（如果你找到了，请在issues留言，并大骂作者是铸币）

我们自己写一个转化方法：

![image](https://github.com/user-attachments/assets/0600c889-70a5-4377-a19e-692064b076f7)

现在我们修改fromJson：

![image](https://github.com/user-attachments/assets/18692890-bbe2-45ef-a5fd-793823cc0ddc)

请记住这个阶段写的读取格式，写配方文件的时候需要使用。

接下来是toNetwork：

![image](https://github.com/user-attachments/assets/8af32813-b611-4586-9c2c-8e1ac0370d83)

可以自行选择编码格式。

现在写fromNetwork：

![image](https://github.com/user-attachments/assets/a57fd5a6-050f-41eb-bee4-05f7c428bc1e)

一样地，保持和toNetwork顺序一致即可。

现在我们在ExampleMachineBlockEntity内更改配方逻辑：

![image](https://github.com/user-attachments/assets/621592f3-ec82-40f7-ba67-bc26ce1d89ba)

然后修改tick逻辑：

![image](https://github.com/user-attachments/assets/1526a17b-826c-44f5-a75e-61ab44404159)

这一步很重要的一点是在输出之前一定要检测是否有足够空间。

现在我们可以添加测试配方：

![image](https://github.com/user-attachments/assets/32c7621a-940f-429d-9be6-0cf69d95f644)

这是一个简单的矿物处理配方，消耗1000mB岩浆将1粗铁转化为2铁锭，并产生1000mB水。

让我们进入游戏，向机器添加一个粗铁：

![image](https://github.com/user-attachments/assets/6f04c0e5-8960-46a4-96cc-46440ff7d5ba)

大功告成。

[**点此进入下一章节**](/tutorial/energy.md)
