# 正文与辅助说明层级统一设计

## 1. 概述

本轮设计聚焦正文与辅助说明层级，把地图页、岛屿面板、课中页、奖励页以及外围页面里分散的 `bodyLarge / bodyMedium / titleMedium / labelLarge` 与各种 `onSurface.copy(alpha = ...)` 收成一套明确的正文语义。目标不是一次性重做所有 renderer 文案，而是在不改变业务行为和稳定测试合同的前提下，让卡片说明文、摘要文和页面辅助提示读起来属于同一套 typography system。

## 2. 设计目标

- 建立可复用的正文语义，而不是继续在 feature 中随意混用 `bodyLarge/bodyMedium/titleMedium`。
- 建立少量文字色调语义，减少分散的 alpha 常量。
- 保持现有文案、tag 和业务行为不变。

## 3. 非目标

- 不统一 renderer 内所有细碎文本。
- 不改按钮文案、状态标签或标题层级。
- 不修改业务逻辑、测试 tag 和交互行为。

## 4. 正文语义

本轮先收 4 级：

### 4.1 bodyLead

用于卡片中最重要的第一段说明或摘要。

### 4.2 bodyPrimary

用于正常正文和常规说明。

### 4.3 bodySecondary

用于次级提示、补充说明、流程说明。

### 4.4 supportingLabel

用于小范围辅助说明、轻提示。

## 5. 文字色调语义

新增轻量色调语义，避免继续散落 `alpha = 0.82f / 0.84f / 0.78f / 0.9f`：

- `High`
- `Medium`
- `Low`
- `Supporting`

## 6. 实现策略

### 6.1 TypographyTokens 扩展

在 `ui/theme/TypographyTokens.kt` 中扩展：

- `BodyLead`
- `BodyPrimary`
- `BodySecondary`
- `SupportingLabel`

### 6.2 TextToneTokens

新增 `ui/theme/TextToneTokens.kt`，提供统一文字色调。

### 6.3 首批接入范围

优先卡片说明文：

- `ui/components/TabletInfoCard.kt`
- `ui/components/TabletActionCard.kt`
- `feature/island/IslandStoryCard.kt`
- `feature/level/RewardOverlay.kt`

然后页面辅助说明：

- `feature/home/HomeTabletScreen.kt`
- `feature/level/LevelTabletScreen.kt`
- `feature/chest/ChestTabletScreen.kt`
- `feature/map/MapProgressFeedback.kt`

## 7. 稳定合同

本轮不得改动：

- 文案内容
- 测试 tag
- 主流程行为与状态机

## 8. 验收标准

- 卡片说明文和页面辅助提示不再主要依赖分散的 `bodyLarge/bodyMedium` 和零散 alpha。
- 主路径和外围页面的正文层级明显更一致。
- 文案、tag、业务行为不回归。
