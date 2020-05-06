# ItemCF
个性化推荐（基于物品的协同过滤算法）
[个性化推荐_视频Demo](https://github.com/xuzichang/UserCF/blob/master/rec%20demo.mp4)

## 一、原理
> 1. 计算物品之间的相似度
> 2. 根据物品的相似度和用户的历史行为给用户生成推荐列表

## 二、算法实现和应用
### 1、计算物品之间的相似度[余弦相似度]
设 N(u) 表示喜欢物品u的用户数，| N(u) ⋂ N(u) |表示同时喜欢物品u物品v的用户数，那么物品 u 和 v 的相似度为
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/CodeCogsEqn.png" text-align="center"/></div>

以Demo为例，为用户1001推荐商品。

<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/table.png" text-align="center" height="300px"/></div>

#### 1.1、建立用户-物品倒排表
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/user-item.png" text-align="center" height="200px" height="400px"/></div>

```java  
  
//这里放你的代码
  
```

#### 1.2、计算共现矩阵，N[i]表示喜欢物品i的人数
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/gongxianjuzhen.png" text-align="center" height="200px"/></div>


<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/usercount.png" text-align="center" height="50px"/></div>

#### 1.3、计算余弦相似度矩阵W
当前用户1001点赞的物品ID为1，由共现矩阵可得Nij。（跳过当前用户1001喜欢得物品1）
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/Nij.png" text-align="center" height="50px"/></div>

根据余弦相似度公式，得Wij。
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/Wij.png" text-align="center" height="50px"/></div>

### 2、根据物品的相似度和用户的历史行为给用户生成推荐列表
此时推荐得物品ID为3，4，5。

### 3、推荐列表不足5个得按点赞数补充。

简单过程（具体之后补）
<img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/userCF.jpg" width="750"/>
