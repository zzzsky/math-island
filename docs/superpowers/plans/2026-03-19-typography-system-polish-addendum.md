# Typography System Polish Addendum

## 背景

当前 `surface / action / status` 已经逐步统一，但标题层级还没有系统化：`Type.kt` 还是空实现，页面里仍有 `42.sp` 和分散的 `headline/title` 混用。

## 新增目标

- 建立基础 typography
- 建立语义化标题 token
- 迁移主路径和外围页面的标题层级

## 并发任务

### 1. typography-foundation

- 完成 `Type.kt`
- 新增 `TypographyTokens.kt`

### 2. main-flow-heading-alignment

- 迁移 map / island / level / reward 的标题层级

### 3. peripheral-heading-alignment

- 迁移 home / chest / parent summary 的标题层级

## 验收

- 主路径和外围页面使用同一套标题语义
- 文案和 tag 不变
- 阶段收口时统一跑 `testDebugUnitTest`、`connectedDebugAndroidTest`、`assembleDebug`
