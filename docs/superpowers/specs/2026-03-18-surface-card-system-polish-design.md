# 数学岛 Surface/Card 系统统一设计

## 1. 概述

本设计聚焦主路径页面中的 `surface/card` 层级统一，把地图页、岛屿详情面板、课中页和奖励页中分散的表面色、圆角、描边和卡片层级收成一套共享语义。目标不是做一整套庞大的 design system，而是先统一最常用的三层 card 语义，让页面之间看起来像同一个产品，而不是多批 UI 叠加的结果。

## 2. 设计目标

- 建立共享的 `page surface / primary card / secondary card` 三层语义。
- 把主路径页面从“直接写死色值和圆角”切到“声明式使用共享 surface 语义”。
- 减少 feature 文件里重复出现的 `Color(0xCC...) + RoundedCornerShape(...) + border(...)` 组合。
- 保持现有交互、测试 tag 和业务状态不变。

## 3. 非目标

- 不在本轮统一所有 button、chip 或 spacing 语义。
- 不引入复杂的 design system provider 或 CompositionLocal 层级。
- 不改动 challenge 规则、地图反馈规则或课程逻辑。

## 4. 统一对象

### 4.1 Page Surface

页面底层大容器使用的表面层级。

适用区域：

- 地图页主区域大容器
- 课中页左右主列的大容器
- 奖励页主弹层的大容器

职责：

- 提供最深一层表面背景
- 定义页面级圆角和描边基线

### 4.2 Primary Card

页面里最重要的一层内容卡。

适用区域：

- 岛屿详情主面板
- 课中页题目信息主卡
- 奖励页主结果卡

职责：

- 承载主要信息
- 与 page surface 保持清晰层级差

### 4.3 Secondary Card

次级信息卡、补充卡、列表卡。

适用区域：

- 地图页左侧岛屿列表卡
- 岛屿面板里的 story card、lesson card
- 课中页辅助信息卡
- 奖励页统计卡、提示卡

职责：

- 表示辅助信息
- 支持更高密度使用

## 5. 代码结构

### 5.1 SurfaceTokens

新增共享 token 文件，集中定义：

- 三层 surface 的默认颜色
- 默认圆角等级
- 默认描边颜色/透明度
- 卡片标准内边距值

### 5.2 SurfaceCard

新增轻量共享组件，用声明式方式表达：

- 这是 page / primary / secondary 哪一层
- 是否需要自定义描边
- 是否需要自定义内边距

它不应该承载业务逻辑，只负责把 shared surface 语义落到具体 `Card` 表现。

### 5.3 现有组件接入

优先接入这些现有点：

- `StoryPanelCard`
- `TabletInfoCard`
- `TabletActionCard`
- `TabletStatTile`
- `MapTabletScreen`
- `IslandOverlaySheet`
- `LevelTabletScreen`
- `RewardOverlay`

## 6. 集成策略

### 6.1 地图与 overlay

- 地图页主框继续是 page surface
- 左侧列表卡、右侧 story/lesson card 统一视为 secondary card
- 右侧主 overlay 容器视为 primary card

### 6.2 课中与奖励

- 课中左右主容器视为 page surface
- 题目和结果主卡视为 primary card
- 统计卡、提示卡视为 secondary card

这样地图线和 level/reward 线会逐步共享同一套表面层级。

## 7. 测试与合同

本轮不新增业务合同，只要求：

- 原有稳定 tag 继续存在
- 页面间视觉层级更一致
- 组件改造不导致主流程测试回归

需要保住的主合同包括：

- `map-open-chest`
- `map-total-stars`
- `select-island-*`
- `panel-start-*`
- level/reward 现有主按钮和统计卡合同

## 8. 验收标准

- 地图页、overlay、level、reward 使用同源的 surface/card 层级
- feature 文件里硬编码 surface 细节明显减少
- 主路径 UI 看起来像一套系统，而不是多个独立批次
- 整体行为和测试合同不回归
