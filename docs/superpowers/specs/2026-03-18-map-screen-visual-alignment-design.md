# 数学岛地图页视觉统一设计

## 1. 概述

本设计聚焦地图页内部的视觉统一，把左侧岛屿列表卡、顶部工具条和右侧岛屿详情面板收成一套完整的视觉语言。目标不是增加新玩法，而是在不改变地图交互和课程入口行为的前提下，让地图页不再像三套独立 UI 拼接而成。

本轮继续复用已经落地的岛屿 icon 资源链路、地图 token 和面板 token，不新增业务状态，不改变焦点切换、宝箱入口和课程入口合同。

## 2. 设计目标

- 左侧 `map-islands-list` 的岛屿卡与右侧面板共享同一套 island icon 和状态视觉。
- 顶部工具条中的返回首页、打开宝箱、总星星 pill 与地图整体风格统一。
- `select-island-*`、`map-open-chest`、`map-total-stars` 等合同保持稳定。
- `MapTabletScreen` 不再自己堆大量样式细节，而是改由更聚焦的地图页组件负责表现。

## 3. 非目标

- 不新增课程入口按钮到左侧岛屿卡。
- 不修改地图焦点切换逻辑。
- 不修改宝箱逻辑、星星逻辑或地图反馈事件。
- 不在这轮继续扩张地图动画或面板插画资源。

## 4. 视觉方向

继续沿用当前“轻手绘冒险感”，但把地图页三块区域统一成一套系统：

- 顶部工具条：像探索控制条，保留木牌/纸卡感。
- 左侧岛屿卡：像小型岛屿任务卡，与右侧大面板同源。
- 右侧面板：保持当前已经统一好的 header / story / lesson card 风格。

统一重点不在“更华丽”，而在“更像同一个产品层次”。

## 5. 组件拆分

### 5.1 MapIslandListCard

负责：

- 左侧岛屿列表中每个岛屿卡的表现
- 岛屿 icon
- 标题、副标题
- 状态 chip
- 进度条

约束：

- 继续使用 `select-island-<id>` 作为交互入口
- 不放课程 CTA
- 点击行为仍然只是切换焦点

资源来源：

- 复用 `MapArtRegistry.resolveIslandArt(island.id)` 的 icon 资源

### 5.2 MapTopBar

负责：

- 返回首页按钮
- 打开宝箱按钮
- 总星星 pill

约束：

- 保持现有 tag：
  - `map-open-chest`
  - `map-open-chest-pulse`
  - `map-total-stars`
  - `map-total-stars-pill`
- 不改反馈时序和动作回调

### 5.3 MapScreenTokens

负责：

- 地图页外层容器、top bar 和列表卡的补充 token

约束：

- 岛屿卡优先复用 `IslandPanelTokens`
- 只有地图页局部细节才放到 `MapScreenTokens`

## 6. 复用策略

### 6.1 图标复用

左侧岛屿卡与右侧面板必须共用同一个 island icon 资源链路：

- 同一个 `island_<id>_icon`
- 同样优先 drawable，缺失时同样 fallback

### 6.2 状态复用

岛屿状态表达尽量保持一致：

- `当前焦点`
- `已解锁`
- `已完成`
- `等待前岛完成`

视觉上不要求完全一模一样，但必须是同一色系和 token 语义，而不是重新起一套颜色规则。

### 6.3 卡片容器复用

左侧岛屿卡优先复用现有纸卡/面板风格：

- 与 `IslandPanelHeader` / `IslandStoryCard` 使用同源圆角、描边和表面色
- 不直接复制 overlay 的完整布局
- 允许保留列表卡更紧凑的密度

## 7. 集成边界

`MapTabletScreen` 继续作为地图页主集成入口，但职责应进一步收窄：

- 保留状态编排、反馈消费和布局区域组织
- 视觉层由 `MapTopBar`、`MapIslandListCard` 和现有 `IslandOverlaySheet` 承担

这样后续如果继续 polish 地图页，可以单独改 top bar、列表卡或右侧面板，而不需要每次都进大文件。

## 8. 测试与合同

本轮必须保持以下合同不变：

- `select-island-<id>`
- `map-open-chest`
- `map-open-chest-pulse`
- `map-total-stars`
- `map-total-stars-pill`
- `panel-start-<lessonId>`

需要补强的测试：

- `MapTabletScreenTest`
  - 左侧岛屿卡可见且切换焦点仍生效
  - top bar 合同仍存在
  - 右侧面板仍跟随选中岛更新
- `MathIslandTabletFlowTest`
  - 主线地图 -> 面板 -> 课程进入不回归
  - 解锁后地图反馈和面板入口不回归

## 9. 验收标准

- 地图页顶部、左侧列表卡、右侧面板形成统一视觉语言
- 左侧列表卡不再是另一套独立样式
- 交互行为和稳定 tag 不回归
- `MapTabletScreen` 的视觉细节进一步收缩到专门组件中
