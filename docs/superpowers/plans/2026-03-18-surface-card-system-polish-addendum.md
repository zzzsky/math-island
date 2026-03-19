# 数学岛 Surface/Card 系统统一补充计划

> 本文档补充当前 tablet UI polish 主线，聚焦 `surface/card` 层级统一。

## 1. 目标

在不改变主路径行为的前提下：

- 建立 page / primary / secondary 三层 surface 语义
- 把地图页、overlay、level、reward 接到同一套表面系统
- 减少 feature 内的硬编码 surface 细节

## 2. 任务拆分

### Task A: 共享 surface 基建

**文件：**

- Add: `app/src/main/java/com/mathisland/app/ui/theme/SurfaceTokens.kt`
- Add: `app/src/main/java/com/mathisland/app/ui/components/SurfaceCard.kt`
- Update: `app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt`

**结果：**

- 共享 page / primary / secondary 语义落地
- `StoryPanelCard` 改走共享 surface 基建

### Task B: 地图与 overlay 接入

**文件：**

- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/island/*`

**结果：**

- 地图与 overlay 使用同源 surface 层级

### Task C: level / reward 接入

**文件：**

- Update: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Update: shared `TabletInfoCard / TabletStatTile / TabletActionCard` if needed

**结果：**

- 课中页和奖励页接到同一套 surface 系统

## 3. 推荐顺序

1. 共享 surface 基建
2. 地图与 overlay 接入
3. level / reward 接入

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
