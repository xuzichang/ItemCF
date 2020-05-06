# UserCF
个性化推荐（基于用户的协同过滤算法）
[个性化推荐_视频Demo](https://github.com/xuzichang/UserCF/blob/master/rec%20demo.mp4)

## 一、原理
当一个用户 A 需要个性化推荐时，可以先找到和他兴趣相似的用户群体 G，然后把 G 喜欢的、并且 A 没有听说过的物品推荐给 A。
>  1. 找到与目标用户兴趣相似的用户集合
>  2. 找到这个集合中用户喜欢的、并且目标用户没有听说过的物品推荐给目标用户
### 1、发现兴趣相同的用户[余弦相似度]
设 N(u) 为用户 u 喜欢的物品集合，N(v) 为用户 v 喜欢的物品集合，那么 u 和 v 的相似度为$ w =  \frac {|N(u)\bigcap N(v)|} {\sqrt |N(u)|\times |N(v)| } $
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/CodeCogsEqn.png" text-align="center" height="200"/>
  </div>
简单过程（具体之后补）
<img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/userCF.jpg" width="750"/>
