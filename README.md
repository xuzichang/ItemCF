# ItemCF
个性化推荐（基于物品的协同过滤算法）
[个性化推荐_视频Demo](https://github.com/xuzichang/UserCF/blob/master/rec%20demo.mp4)

## 一、原理
> 1. 计算物品之间的相似度
> 2. 根据物品的相似度和用户的历史行为给用户生成推荐列表
### 1、计算物品之间的相似度[余弦相似度]
设 N(u) 表示喜欢物品u的用户数，| N(u) ⋂ N(u) |表示同时喜欢物品u物品v的用户数，那么物品 u 和 v 的相似度为
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/CodeCogsEqn.png" text-align="center"/></div>

以Demo为例，为用户1001推荐商品。

<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/table.png" text-align="center" height="400px"/></div>
第一步：跳过当前用户。
喜欢物品0的


简单过程（具体之后补）
<img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/userCF.jpg" width="750"/>
