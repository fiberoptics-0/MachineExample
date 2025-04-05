
第八步：收尾工作

我们首先得修复GUI的互动逻辑。让我们继承SlotItemHandler，然后自己重写三个方法：

![image](https://github.com/user-attachments/assets/1d2a80c9-7ca5-439b-8cc0-ae811b819c2e)

这样游戏在判断玩家能否放入/取出物品时就不会按照ModItemStackHandler的逻辑进行检测了。

让我们把这段代码应用到Menu逻辑中：

![image](https://github.com/user-attachments/assets/aed2a3d6-f03b-4a0c-833b-201279e531cf)

现在再进入游戏，我们会发现玩家可以正常地和两个物品槽互动了。

尽管如此，shift快速移动的功能还是不存在。让我们在ExampleMachineMenu中加入如下代码：

![image](https://github.com/user-attachments/assets/db011d74-00be-48ff-8fdf-6be81397932f)

请注意这里 9+27+1 和 9+27+2 的区别。我们规定只有机器的第一个栏位能接受快速移动的物品，而两个栏位都可以向外快速移动物品（可以参考一下原版熔炉的逻辑：你可以按住shift键将物品放入/拿出输出槽或是拿出输入槽，但是无法放入输出槽。）

现在进入游戏，会发现shift逻辑也正常了。

现在，我们希望玩家在鼠标放到能量或流体槽上时能看到对应的信息。

在ExampleMachineScreen中加入以下重写：

![image](https://github.com/user-attachments/assets/f554a7f3-20c2-4c76-b4d7-d8ac554cd293)

并添加lang文件条目：

![image](https://github.com/user-attachments/assets/9af46ec8-3427-40a3-94c4-de7615b69a51)

效果如下：

![image](https://github.com/user-attachments/assets/15e4227c-343e-4099-bce4-991a5af45810)

下一步是流体槽位：

![image](https://github.com/user-attachments/assets/dd40f716-827c-4913-889c-9a1ebdd99256)

记得添加lang文件条目：

![image](https://github.com/user-attachments/assets/0385a6c7-5f99-48d8-9389-2db7482d181b)


效果如下：

![image](https://github.com/user-attachments/assets/d9cbd1e5-2dc3-4655-9bf9-193ecf16f466)
