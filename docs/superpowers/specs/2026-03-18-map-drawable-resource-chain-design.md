# 数学岛地图 Drawable 资源链路设计

## 1. 概述

本设计聚焦地图本体的默认资源接入链路：把已经存在的地图美术插槽从“只有 registry 和 fallback painter”推进到“优先读取 `res/drawable` 的 canonical 资源名，找不到时再回退到 Compose painter”。本轮不改地图交互、课程入口、解锁逻辑和测试标签，只补资源链路、占位资源和回归测试。

目标不是一次接入正式插画，而是把 `vector drawable` 作为默认占位资源打通，让后续美术资源可以按同名替换，而不需要再改 registry 或地图交互层。

## 2. 设计目标

- `MapArtRegistry` 能按 canonical slot key 解析 `res/drawable` 中的默认资源。
- 所有地图核心资源槽位都有默认 `vector drawable` 占位。
- 找不到资源时，现有 Compose painter fallback 仍然生效。
- 保持 `map-node-*`、`map-route-highlight-*`、`panel-start-*` 等合同不变。

## 3. 非目标

- 不接入正式 PNG/WebP 插画资源。
- 不改变地图布局、节点位置、热区和交互行为。
- 不修改课程、奖励页或右侧岛屿面板的业务逻辑。
- 不在本轮引入新的动画系统。

## 4. 默认资源接入链路

### 4.1 资源来源顺序

对每一个地图资源槽位，统一采用以下顺序：

1. 通过 canonical slot key 读取 `res/drawable` 中的同名资源
2. 如果资源不存在，则回退到现有 Compose painter fallback

这条规则对以下资源类型都一致：

- 海面背景
- 航线默认态与高亮态
- 岛屿 base 图
- 岛屿 icon
- 共享状态 overlay

### 4.2 canonical key 规则

资源名和 slot key 必须一一对应，不新增别名：

- `map_sea_backdrop`
- `route_segment_default`
- `route_segment_highlight`
- `island_<normalizedIslandId>_base`
- `island_<normalizedIslandId>_icon`
- `island_locked_overlay`
- `island_unlocked_tint`
- `island_focused_ring`
- `island_completed_badge`

其中 `normalizedIslandId` 继续使用当前规则：

- 来自 curriculum 的 `islandId`
- 所有 `-` 转成 `_`
- 小写 ASCII

## 5. 资源组织

本轮全部资源先落到：

- `app/src/main/res/drawable/`

不单独建新资源模块，也不拆到 `drawable-*dpi`。原因是这些资源当前只是默认占位资源，不是终态美术；先保持链路简单，后续再按需要做密度或格式扩展。

## 6. 默认占位资源清单

### 6.1 海面

- `map_sea_backdrop.xml`

视觉要求：

- 轻手绘海面底纹
- 可与当前 `SeaBackdropPainter` 的 fallback 共存
- 不承载文字

### 6.2 航线

- `route_segment_default.xml`
- `route_segment_highlight.xml`

视觉要求：

- 默认态偏纸面虚线/柔和曲线
- 高亮态偏暖色强调线
- 允许拉伸到不同长度

### 6.3 共享状态 overlay

- `island_locked_overlay.xml`
- `island_unlocked_tint.xml`
- `island_focused_ring.xml`
- `island_completed_badge.xml`

视觉要求：

- `locked`：灰蓝遮罩
- `unlocked`：暖色轻 tint
- `focused`：手绘聚焦 ring
- `completed`：贴纸式徽章

### 6.4 七座岛 base 图

- `island_calculation_island_base.xml`
- `island_measurement_geometry_island_base.xml`
- `island_multiplication_island_base.xml`
- `island_division_island_base.xml`
- `island_big_number_island_base.xml`
- `island_classification_island_base.xml`
- `island_challenge_island_base.xml`

视觉要求：

- 每座岛有不同的主题色和轮廓感
- 不承载文字
- 保持缩小后仍可区分

### 6.5 七座岛 icon

- `island_calculation_island_icon.xml`
- `island_measurement_geometry_island_icon.xml`
- `island_multiplication_island_icon.xml`
- `island_division_island_icon.xml`
- `island_big_number_island_icon.xml`
- `island_classification_island_icon.xml`
- `island_challenge_island_icon.xml`

视觉要求：

- 小尺寸主题符号
- 优先简单 vector 形状
- 未来可被正式 icon 资源同名替换

## 7. 代码结构

### 7.1 MapArtRegistry

`MapArtRegistry` 继续作为唯一入口，但需要新增资源解析逻辑：

- 根据 slot key 解析 Android drawable 资源 ID
- 找到时返回资源 painter
- 找不到时返回 fallback painter

重要约束：

- `MapArtSource` 对外接口不变
- 仍然只做读取，不存业务状态
- 不在 `IslandMapCanvas` 内散落 `painterResource` 之类的资源解析逻辑

### 7.2 Painter 层

`SeaBackdropPainter`、`IslandNodePainter`、`RoutePainter` 继续只做表现层。它们不负责查找资源名，只消费 registry 返回的 art。

### 7.3 测试边界

回归测试要覆盖三件事：

- 有资源时，registry 能拿到资源 painter
- 没资源时，fallback 仍可用
- 地图交互与 tag 合同不变

## 8. 测试与验收

需要覆盖：

- `MapArtRegistryTest`
  - canonical key 与 drawable 解析正确
  - 资源缺失时仍有 fallback
- `IslandMapCanvasTest`
  - 有资源和 fallback 两条路径下，节点/高亮 tag 都存在
- `MapTabletScreenTest`
  - 屏幕级地图交互合同不变
- `MathIslandTabletFlowTest`
  - 解锁流、反馈流、课程入口合同不变

## 9. 验收标准

- 所有地图核心 slot 都存在默认 `vector drawable` 占位
- `MapArtRegistry` 默认优先走 drawable 资源链路
- 缺失资源不会导致地图空白、崩溃或交互回归
- 现有地图语义节点、测试标签和课程入口合同保持稳定
