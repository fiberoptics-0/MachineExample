
# 第七步：眼见为实（Screen）

好的，现在我们已经有了一个能处理各种配方的机器，但是这里有些问题：不用/data指令或是物流提取的话，我们根本无法知道机器的内容物是什么，更别提有些槽位根本无法被提取。

因此，我们希望能给机器加上GUI，让玩家可以清晰地看到其内容物。

在开始编写代码之前，我们需要先确定gui的外观。严格来讲，你可以在这里整很多花活，但是我们还是先参考一个已有的例子，比如原版熔炉：

![image](https://github.com/user-attachments/assets/212c0fa1-5da9-4ff0-bb48-9f10baa89e91)

把这个材质扔进画图软件，稍微改一改，我们就可以获得如下材质：

![image](https://github.com/user-attachments/assets/89d76cb5-5b82-4328-91b9-2150ff4631da)

画完材质之后一定要确认好各种物品栏位和gui构件的（左上角）坐标，待会要用到。

好的，现在开始进行逻辑实现。

一般来说，大部分gui都需要先实现网络传输，因为机器逻辑一般在服务端执行，而gui渲染则只在客户端进行。然而，我们手上有一个现成的工具：AbstractContainerMenu。这是原版mc所有容器（比如箱子，熔炉）gui的共同父类，和客户端类AbstractContainerScreen共同集成了必要的数据同步。

要实现我们的Menu，首先我们给我们的ExampleMachineBlockEntity定义一个ContainerData：

![image](https://github.com/user-attachments/assets/cdd13a8d-1cad-4bea-8197-329adb1e229a)

这个类负责客户端向服务端的数据自动同步，可惜只能传输整数。

在构造器里初始化data：

![image](https://github.com/user-attachments/assets/ca2ba822-69ba-42fa-a31d-f1d8a3843ae7)

现在我们来写我们的Menu：

![image](https://github.com/user-attachments/assets/3d7cd07b-05b1-4102-9801-2706c5029d8d)

这里是一些必要的常量。接下来是构造器：

![image](https://github.com/user-attachments/assets/4decbad7-ffe9-4e9c-894f-fb5ef4bc2b66)

这里填入的坐标即是gui材质中上方的两个物品格子的坐标。其中addPlayerInventory和addPlayerHotbar两个方法我们得待会自己实现。

![image](https://github.com/user-attachments/assets/178db8cb-02ca-4fde-836e-6485ab756270)

这个是注册时要用到的构造器，直接调用另外一个构造器。

![image](https://github.com/user-attachments/assets/f9f8352d-04b9-4e24-b0ef-66da9732cc38)

这两个方法告诉游戏玩家身上的物品栏和快捷栏该在哪里渲染。只要这两个栏的位置不变，这两个方法就不用变。

![image](https://github.com/user-attachments/assets/f5c73e43-1323-4b09-91b0-03b298df3b5a)

quickMoveStack实现了按住Shift点击的快速移动逻辑。我们先跳过这个，让他返回ItemStack.EMPTY

stillValid在页面已不适用的时候（比如容器已被破坏）告诉游戏关闭页面。可以直接调用自带方法。

现在我们可以开始写Menu对应的客户端Screen了：

![image](https://github.com/user-attachments/assets/921186e9-45bb-4eda-a6c1-ec072af922e4)

首先是我们要用到的材质和简明易懂的构造器。

![image](https://github.com/user-attachments/assets/251101c5-f29e-4ef0-a502-9ffcdc235a8e)

接下来是背景层的绘画。这一层在物品渲染层下面。其中前三行是一些必要的设置，比如颜色之类的。

PoseStack承载了渲染器的一些位置变化，比如平移，旋转，缩放等。

pushPose方法会把当前的PoseStack存入一个栈内。调用popPose会从栈内弹出一个PoseStack状态，覆盖掉当前的状态，在绘图中需要频繁撤销移动操作的情况下很好用。

（注意：绘图结束之后栈内不能留有状态，也就是说每一个pushPose都必须有一个popPose与其对应，否则会报错崩溃）

blit方法会从特定材质中截取一个选定的矩形，画到屏幕的指定位置上。

我们在这里先是用blit将gui基底画到屏幕上，然后根据ContainerData返回的数据决定了进度箭头的截取长度，随后又使用blit绘制了箭头。

现在我们重写一下render方法：

![image](https://github.com/user-attachments/assets/0d34426a-5a86-4a30-a0c5-f6cf03ac5d91)

请注意这里的渲染顺序。假如我们把tooltip的渲染放在第一位的话，tooltip将不会显示，因为背景和物品会盖过它。其他的顺序同理。

现在Screen也已经完成了。我们给ExampleMachineBlockEntity增加一个接口实现：MenuProvider

![image](https://github.com/user-attachments/assets/b7ee7f50-4675-43b0-aa42-5b90f4c32997)

这样我们会多两个方法要实现：

![image](https://github.com/user-attachments/assets/203bbd25-4013-4c26-b6e1-5f710e596c4f)

还好这些都比较一目了然。现在我们得将Menu和Screen注册。首先是Menu：

![image](https://github.com/user-attachments/assets/b17ce1aa-827f-4a35-9e91-0cd05482921e)

然后是Screen。这个得用事件监听注册：

![image](https://github.com/user-attachments/assets/ca8553d4-d038-4a2c-ae83-fa23400ccb9e)

现在万事具备了。最后一步是在ExampleMachineBlock里告诉游戏在玩家右键的时候打开相应gui：

![image](https://github.com/user-attachments/assets/01cf998c-6f59-46f7-97ae-81d92d9866c5)

现在让我们进入游戏：

![image](https://github.com/user-attachments/assets/8935beab-fe96-48d0-a32d-5c112c7d90ca)

你很快就会发现这个gui有些问题。比如，输入栏内的物品无法被取出，以及只要输入栏内已经有物品了，就无法添加更多的同类物品。

这是因为AbstractContainerMenu的玩家互动逻辑是根据Capabilities的互动逻辑进行的，而在ItemHandler逻辑已被修改过的情况下，如果不自定义Menu逻辑，很多地方就会。。。嗯，变得奇奇怪怪。

但是我们先不管这个，先把所有的元素都加到gui里再说。

下一个要加入的是能量。因此我们先画一个对应的gui材质：

![image](https://github.com/user-attachments/assets/d7635cd6-f20c-47d9-9dad-7dd149f02bea)

（是的，作者知道自己一点艺术细胞都没有，此事休要再提。）

然后修改ContainerData，让它也能输出和能量有关的数据。

![image](https://github.com/user-attachments/assets/d2217dcd-0d95-4250-a115-5636aac9e500)

顺手再把能量容量翻个一百倍，不然一瞬间能量就满了效果不明显：

![image](https://github.com/user-attachments/assets/f93c4b0b-9153-4dd7-8643-cb87f2d43d58)

然后在ExampleMachineScreen的renderBg方法内加入能量条渲染：

![image](https://github.com/user-attachments/assets/4cd666d9-0515-4218-9f8c-ef9fdf8dece1)

其实和进度条差不多。

记得修改ExampleMachineMenu的构造器，不然游戏要炸（别问我怎么知道的）：

![image](https://github.com/user-attachments/assets/b04c180f-5633-48bc-af56-33c255d43ab7)

现在让我们进入游戏：

![image](https://github.com/user-attachments/assets/fac106a2-08b2-45b4-a7af-fd6a783735bc)

随着能量增加，这个红色的条也会随之增长：

![image](https://github.com/user-attachments/assets/8cb10aa6-f778-4ff9-a934-ec869a8bdb42)

下一步是流体。

大部分互动逻辑都在服务端进行，因此如果我们在Screen渲染的时候（也就是在客户端）试图获取机器内部流体，我们只会得到FluidStack.Empty。

流体类型很难通过ContainerData传递，因此我们得用点其他方法：

![image](https://github.com/user-attachments/assets/03157591-1060-425f-81de-22617089b69c)

这个方法在ExampleMachineBlockEntity内，用来生成一个作同步用的nbt。

![image](https://github.com/user-attachments/assets/86b804ec-45d3-4328-ac6f-e5a74238bd43)

这个方法会根据上一步的nbt生成一个同步用的数据包。

发送数据包时一定要谨慎，避免占用不必要的通信容量。我们在tick逻辑里设置只在流体渲染高度发生改变的时候发送数据包：

![image](https://github.com/user-attachments/assets/cc0be23f-ba06-4152-a50b-aa981e3e4a50)

其中获取渲染高度的方法需要自己定义：

![image](https://github.com/user-attachments/assets/a7d7796e-d58d-4dcd-ab68-9cf64e256f0c)

现在让我们回到ExampleMachineScreen，加一个渲染流体的drawFluid方法：

![image](https://github.com/user-attachments/assets/4a9c080d-0487-45a9-9d98-60d064402198)

这段看上去非常吓人的代码其实原理很简单：我们能读取的流体材质只有一个16x16的方格，因此我们先用这个方格尽可能多地填充想要填充的空间，然后用这个方格裁切后的小矩形填充剩下的空间。

其中vertexBuffer.vertex方法决定了每一次要填充的空间在gui上和在流体材质上分别的范围。

（请务必记得在完成绘制之后把Shader颜色改回去，不然整个物品栏都会变色）

由于我们一共就只有两个流体要渲染，我们在这里偷个懒，直接复制粘贴一份，稍微改改：

![image](https://github.com/user-attachments/assets/7e5a4ced-2ac5-48b2-87b9-e5fd9e23b616)

（致读者中的Java初学者：这是一个很不好的编程习惯，作者就是个懒狗，请不要学他）

现在我们把我们的方法添加到renderBg的最后：

![image](https://github.com/user-attachments/assets/3ce966c1-ce9b-4d8a-876d-995eadc47c6e)

现在让我们进入游戏：

![image](https://github.com/user-attachments/assets/75522ac6-43cc-4559-82ed-c4233c4d9f4e)

[**点此进入下一章节**](/tutorial/fix.md)
