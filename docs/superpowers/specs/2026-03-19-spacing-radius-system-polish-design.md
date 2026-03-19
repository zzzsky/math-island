# 间距与圆角系统统一设计

## 1. 概述

本轮设计聚焦主路径与外围页面的间距和圆角统一，把地图页、岛屿面板、课中页、奖励页以及外围页面里分散的 `12 / 14 / 16 / 18 / 20 / 22 / 24 / 28 / 30 / 32 / 999` 收成一套轻量 spacing/radius 系统。目标不是逐像素重排所有页面，而是在不改变业务行为和稳定测试合同的前提下，让页面节奏和容器形态读起来属于同一套设计系统。

## 2. 设计目标

- 建立少量稳定的 spacing token 和 radius token。
- 保持现有文案、业务逻辑、测试 tag 和主要布局结构不变。
- 优先减少主路径和外围页面里最常见的硬编码间距与圆角。

## 3. 非目标

- 不在这轮统一 renderer 内所有细碎间距。
- 不改业务逻辑、交互、动画或测试合同。
- 不追求保留每一个 `14dp` 或 `22dp` 的独立 token；能收敛就收敛。

## 4. Token 范围

### 4.1 SpacingTokens

本轮先收 6 个间距等级：

- `Xs`
- `Sm`
- `Md`
- `Lg`
- `Xl`
- `Xxl`

推荐映射：

- `12 -> Sm`
- `16 -> Md`
- `18 -> Lg`
- `20 -> Xl`
- `24 -> Xxl`

`14 / 22` 不优先保留为独立 token，迁移时优先向相邻等级收敛。

### 4.2 RadiusTokens

本轮先收 4 个圆角等级：

- `Pill`
- `CardMd`
- `CardLg`
- `Sheet`

推荐映射：

- `24 -> CardMd`
- `28 -> CardLg`
- `30 / 32 -> Sheet`
- `999 -> Pill`

## 5. 组件策略

### 5.1 Theme Token 文件

新增：

- `ui/theme/SpacingTokens.kt`
- `ui/theme/RadiusTokens.kt`

### 5.2 首批直接接入页面

优先迁移最显眼的页面层级和主容器，不强迫所有小组件一起改：

- 主路径：
  - `feature/map/MapTabletScreen.kt`
  - `feature/island/IslandOverlaySheet.kt`
  - `feature/level/LevelTabletScreen.kt`
  - `feature/level/RewardOverlay.kt`
- 外围页面：
  - `feature/home/HomeTabletScreen.kt`
  - `feature/chest/ChestTabletScreen.kt`
  - `feature/parent/ParentSummaryTabletScreen.kt`

### 5.3 共享组件

在本轮里，优先让以下共享组件先接 token：

- `ui/components/SurfaceCard.kt`
- `ui/components/StatusChip.kt`
- `ui/components/TabletInfoCard.kt`
- `ui/components/TabletStatTile.kt`
- `ui/components/TabletActionCard.kt`

## 6. 稳定合同

本轮不得改动：

- 主流程行为
- 文案
- 现有稳定 test tag
- 动画和状态机逻辑

## 7. 验收标准

- 主路径和外围页面的大部分硬编码间距/圆角减少。
- 页面节奏与卡片形态明显更统一。
- 业务行为、标签和文案不回归。
- spacing/radius token 足够少，便于后续继续维护，不演变成另一套硬编码表。
