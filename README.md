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
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/user-item.png" text-align="center" height="100px"/></div>

#### 1.2、计算共现矩阵，N[i]表示除去当前用户喜欢物品i的人数
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/gongxianjuzhen.png" text-align="center" height="200px"/></div>


<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/usercount.png" text-align="center" height="50px"/></div>

```java  
//初始矩阵
for (int i = 0; i < likeLists.size(); i++) {
    int pid1 = likeLists.get(i).getPid();
    ++N[pid1];                                          //喜欢该id物品的人数加一
    for (int j = i + 1; j < likeLists.size(); j++) {
        int pid2 = likeLists.get(j).getPid();
        ++curMatrix[pid1][pid2];
        ++curMatrix[pid2][pid1];                        //对称加一
    }
}
//累加所有矩阵, 得到共现矩阵
for (int i = 0; i < products.size(); i++) {
    for (int j = 0; j < products.size(); j++) {
        int pid1 = products.get(i).getPid(), pid2 = products.get(j).getPid();
        comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
        comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
    }
}
```
#### 1.3、计算余弦相似度矩阵W
当前用户1001点赞的物品ID为1，由共现矩阵可得Nij。（跳过当前用户1001喜欢得物品1）
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/Nij.png" text-align="center" height="50px"/></div>

根据余弦相似度公式，得Wij。
<div align=center><img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/Wij.png" text-align="center" height="50px"/></div>

这里使用了TreeSet重写compare方法实现添加的商品按点赞数排序。
```java  
TreeSet<Product> preList = new TreeSet<Product>(new Comparator<Product>() {
    //重写compare方法  按相似度Wij排序；当相似度Wij相同时，按点赞数排序
    @Override
    public int compare(Product o1, Product o2) {
        if(o1.getW()!=o2.getW()){
            return (int) ((o1.getW()-o2.getW())*100); //返回值为0，表示同一元素
        }
        else{
            //当相似度相同时，比较点赞数
            return o1.getCnt()-o2.getCnt();
        }
    }
}); //预处理的列表
```
```java  
for(Like like: likeLists){
    int Nij = 0;                         //既喜欢i又喜欢j的人数
    double Wij;                          //相似度
    Product tmp;                           //当前的产品

    int i = like.getPid();

    for(Product product: products){
        if(like.getPid() == product.getPid()) continue;
        int j = product.getPid();
        Nij = comMatrix[i][j];
        Wij = (double)Nij/Math.sqrt(N[i]*N[j]);         //计算余弦相似度

        tmp = productdao.findProductById(product.getPid());

        if(Double.isNaN(Wij)) tmp.setW(0);
        else tmp.setW(Wij);

        if(used[tmp.getPid()]) continue;               //已加入推荐列表

        if(!Double.isNaN(Wij) && Wij!=0){
            preList.add(tmp);
            used[tmp.getPid()] = true;
        }
    }
}
```
### 2、根据物品的相似度和用户的历史行为给用户生成推荐列表
此时推荐的物品ID为3，4，5。
```java  
ArrayList<Product> recomLists = new ArrayList<>();      //生成的推荐结果

for(int i = 0; preList.size()>0 && i<5; i++){
    recomLists.add(preList.pollLast());
}
```
### 3、推荐列表不足5个的按点赞数补充。
```java  
//推荐数量不满5个, 补足喜欢数最高的文章，考虑重复
if(recomLists.size()<5){
    recomLists = productdao.findTopNProducts(recomLists);
}
```
简单过程（具体之后补）
<img src="https://github.com/xuzichang/UserCF/blob/master/ImgForReadme/userCF.jpg" width="750"/>
