# 标题层级统一设计

## 1. 概述

本轮设计聚焦标题层级统一，把地图页、岛屿面板、课中页、奖励页以及外围页面里分散的 `headlineLarge / headlineMedium / titleLarge / 手写 fontSize` 收成一套明确的标题语义。目标不是一次性重做全项目排版，而是在不改业务行为和稳定测试合同的前提下，让页面标题、区块标题和功能标题读起来属于同一套 typography system。

## 2. 设计目标

- 建立可复用的标题语义，而不是继续在 feature 里直接挑 `MaterialTheme.typography.*` 或写死 `fontSize`。
- 保持现有文案、tag 和业务行为不变。
- 让主路径和外围页面的标题层级明显更一致。

## 3. 非目标

- 本轮不统一正文、说明文、caption 和按钮文案。
- 不修改业务逻辑、交互流程、测试 tag。
- 不引入新字体资源。

## 4. 语义层级

本轮只收 4 级：

### 4.1 screenHero

用于首页这类最强页面主标题。

### 4.2 screenTitle

用于页面级标题，例如宝箱页、家长页。

### 4.3 sectionTitle

用于大区块标题，例如地图当前查看标题、奖励页区块标题。

### 4.4 featureTitle

用于功能卡或功能区域里的主标题，例如岛屿面板标题、课中页标题。

## 5. 组件策略

### 5.1 Type.kt

在 `ui/theme/Type.kt` 里真正建立基础 `Typography`，不再使用空的 `Typography()`。

### 5.2 TypographyTokens.kt

新增 `ui/theme/TypographyTokens.kt`，负责把基础字阶映射成语义化标题 token：

- `screenHero`
- `screenTitle`
- `sectionTitle`
- `featureTitle`

feature 文件通过语义 token 用字，而不是自己决定用哪一档 `headline` 或 `title`。

## 6. 首批接入范围

### 6.1 主路径

- `feature/map/MapTabletScreen.kt`
- `feature/island/IslandPanelHeader.kt`
- `feature/level/LevelTabletScreen.kt`
- `feature/level/RewardOverlay.kt`

### 6.2 外围页面

- `feature/home/HomeTabletScreen.kt`
- `feature/chest/ChestTabletScreen.kt`
- `feature/parent/ParentSummaryTabletScreen.kt`

## 7. 稳定合同

本轮不得改动：

- 文案内容
- 测试 tag
- 业务行为和点击路径

## 8. 验收标准

- `Type.kt` 不再是空 typography。
- 页面标题、区块标题、功能标题在主路径和外围页面形成稳定层级。
- feature 文件里硬编码 `fontSize` 和随意挑选 `headline/title` 的情况减少。
- 行为、tag 和文案完全不回归。
