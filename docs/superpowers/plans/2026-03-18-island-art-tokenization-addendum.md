# 数学岛地图本体美术插槽实施补充计划

> 本文档补充 `2026-03-13-math-island-android-tablet-implementation.md` 与 `2026-03-16-map-island-interaction-addendum.md`，聚焦地图本体的手绘冒险感与美术资源插槽。

## 1. 目标

在不改动地图交互合同的前提下，为地图本体建立：

- 手绘冒险感的默认视觉语言
- 可替换的海面/岛屿/航线/状态叠层资源插槽
- 可直接交付美术使用的资源规格文档

## 2. 任务拆分

### Task A: 资源合同与规格

**文件：**

- Add: `app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt`
- Add: `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
- Doc: `docs/superpowers/specs/2026-03-18-island-art-tokenization-design.md`

**结果：**

- 所有地图资源 key、尺寸和 fallback 规则集中定义
- 海面、岛屿、状态叠层、航线、装饰物的规格固定

### Task B: 画布 painter 分层

**文件：**

- Update: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Add: `app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt`
- Add: `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
- Add: `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`

**结果：**

- 岛屿、航线、海面从单文件绘制拆成独立 painter 层
- 每层都支持 `asset or fallback painter`

### Task C: 默认视觉语言升级

**文件：**

- Update: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`

**结果：**

- 地图默认外观升级为轻手绘冒险感
- 交互、测试标签和点击区域保持稳定

## 3. 并行策略

- `Task A` 可先独立完成
- `Task B` 和 `Task C` 可以并发，但 `Task C` 依赖 `Task A` 提供的视觉 token

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
