# 外围页面按钮语义扩展与地图动效抛光设计

## 1. 概述

本轮设计并行推进两条线：

- 低风险线：把 `Home / Chest / Parent` 这些外围页面接到已经落地的 action 语义系统
- 高风险线：把地图当前的轻反馈推进成更完整但仍然可打断的动效体验

两条线同时开发，但验收和合入顺序分开控制。这样既能提高推进效率，又不会把低风险样式统一和高风险动效时序搅在一起。

## 2. 设计目标

- 外围页面按钮不再游离在 action 语义体系之外。
- 地图推进反馈从“有反馈”升级到“更有空间感和节奏感”。
- 主流程、按钮文案、稳定 tag、地图交互和挑战流不变。

## 3. 非目标

- 不继续扩张新的地图业务状态。
- 不重做地图布局结构。
- 不在这轮统一 renderer 内部答题按钮。
- 不修改 Home/Chest/Parent 的业务逻辑。

## 4. 线 A：外围页面 action 语义扩展

### 4.1 目标页面

- `HomeTabletScreen`
- `ChestTabletScreen`
- `ParentGateScreen`
- `ParentSummaryTabletScreen`

### 4.2 目标

把这些页面里的按钮统一到已存在的：

- `primary`
- `secondary`
- `recommended`
- `completed`
- `outlined-secondary`

语义中，而不是继续依赖各页面私有 `ButtonDefaults` 和写死颜色。

### 4.3 接入策略

- `TabletActionCard` 内部 CTA 也接入共享 action 入口
- `Home` 页面中继续冒险、地图、宝箱、家长入口改成语义化按钮
- `Chest` 页面中返回首页、回到地图接入语义
- `ParentGate` 和 `ParentSummary` 的按钮接入语义

### 4.4 约束

- 不改测试 tag
- 不改按钮文案
- 不改页面布局结构

## 5. 线 B：地图动效抛光

### 5.1 当前基线

当前地图已经有：

- 解锁后的节点/航线高亮
- 星星数跳变
- 宝箱按钮短时强调
- 反馈消费一次后消失

### 5.2 本轮目标

在此基础上增强为：

- 新岛解锁时更完整的节点涟漪/外圈脉冲
- 航线高亮更像一次路线扫光，而不只是静态强调
- 顶部反馈和地图高亮的节奏更协调

### 5.3 交互原则

- 所有动效都必须短、轻、可打断
- 用户一旦点击岛屿或离开页面，动效立刻服从交互
- 不引入持久化动画状态

### 5.4 改动边界

- `MapTabletScreen`
- `IslandMapCanvas`
- `MapProgressFeedback`
- 必要时 `ui/components/map/*` 下的 painter

### 5.5 稳定合同

不得改动：

- `map-open-chest`
- `map-open-chest-pulse`
- `map-total-stars`
- `map-total-stars-pill`
- `map-node-*`
- `map-route-highlight-*`
- `panel-start-*`

## 6. 并发与合入策略

### 6.1 并发

两条线可以并发开发，因为写入面基本分离：

- 线 A 主要改外围 feature 和 action 组件
- 线 B 主要改地图页和 map 组件

### 6.2 合入顺序

建议先合线 A，再合线 B。

原因：

- action 语义扩展风险低、容易先稳定
- 地图动效更依赖完整设备回归，后合更安全

## 7. 验收标准

- Home/Chest/Parent 的按钮都进入同一套 action 语义体系
- 地图反馈比当前更有空间感，但交互仍可打断
- 主路径和稳定 tag 不回归
- 阶段收口时单测、instrumentation、assemble 通过
