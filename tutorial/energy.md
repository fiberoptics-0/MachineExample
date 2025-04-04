
# 第六步：抛瓦 is 抛瓦！（IEnergyStorage）

既然我们已经有了物品和流体的相关处理，不妨把能量也加上。

要实现能量存储一样要实现IEnergyStorage接口，但是既然都看到这了，想必都已经驾轻就熟了：

![image](https://github.com/user-attachments/assets/19c8a255-5f2c-4bb4-a4c4-b82fd6a74914)

我们直接继承Forge提供的EnergyStorage类，然后禁用能量提取。

再加上一个我们要用到的方法：

![image](https://github.com/user-attachments/assets/4a9a53a7-bebc-4a49-ac0f-951d1e86d2c1)

现在我们修改用来匹配配方的ModContainer：

![image](https://github.com/user-attachments/assets/084f08f1-2d87-4775-93d2-06ea91d9f799)

然后在配方类ExampleMachineRecipe内加入相应的常量和构造器：

![image](https://github.com/user-attachments/assets/9dd1a7aa-836d-493e-901c-47e6781454a3)

还有一个我们会用到的公共方法：

![image](https://github.com/user-attachments/assets/3ba7c609-7d3c-4bf5-bd4a-13dc5c5e649c)

现在修改matches方法：

![image](https://github.com/user-attachments/assets/3e8080b3-f2e3-4c32-9cb4-310571884ad5)

我们还需要修改RecipeSerializer。首先是fromJson方法：

![image](https://github.com/user-attachments/assets/a2054ed8-b91e-44b2-9828-cdf903d4571b)

然后是toNetwork：

![image](https://github.com/user-attachments/assets/3db418a5-48c8-466f-b16d-7931d60bb4d6)

最后是fromNetwork：

![image](https://github.com/user-attachments/assets/1b3de35a-0a7f-4ff6-91c7-3e725b09b6cb)

配方匹配已经实现，现在让我们向ExampleMachineBlockEntity内加入我们的能量存储：

![image](https://github.com/user-attachments/assets/e3168ea6-0dfa-42a9-8150-64169b2f3948)

然后修改配方搜索：

![image](https://github.com/user-attachments/assets/89bf35b9-ba32-4c6c-a3d5-22617dd5676f)

要实现配方，我们得修改tick方法：

![image](https://github.com/user-attachments/assets/60f43c73-a8ec-481b-a92d-c58c20568f4c)

别忘了nbt存取：

![image](https://github.com/user-attachments/assets/0168769f-fcc8-428e-aade-6da9a3a10bf1)

现在让我们稍微修改一下配方：

![image](https://github.com/user-attachments/assets/e010eb40-bc9e-4a24-ae8c-2cb23e8fd06b)

[**点此进入下一章节**](/tutorial/gui.md)
这个配方之前的基础上多加上了1000单位的能量需求。

让我们进入游戏，用mekanism的发电机和线缆为机器供能：

![image](https://github.com/user-attachments/assets/02158b2e-062a-4a8e-8caf-5f8c32504d43)
