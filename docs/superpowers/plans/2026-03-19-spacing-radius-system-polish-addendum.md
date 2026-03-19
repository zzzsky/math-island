# Spacing & Radius System Polish Addendum

## 背景

标题层级已经统一，但页面节奏和容器圆角还分散在各 feature 文件里，尤其是主路径页面中的 `16 / 18 / 20 / 24 / 28 / 30 / 32 / 999`。

## 新增目标

- 建立轻量 spacing/radius token
- 收掉主路径页面的主要硬编码间距与圆角
- 收掉外围页面最显眼的节奏分叉

## 并发任务

### 1. token-foundation

- 新增 `SpacingTokens`
- 新增 `RadiusTokens`
- 让共享组件优先接 token

### 2. main-flow-spacing-radius-alignment

- 迁移 `map / island / level / reward`

### 3. peripheral-spacing-radius-alignment

- 迁移 `home / chest / parent summary`

## 验收

- 主路径和外围页面节奏更统一
- 行为与测试合同不变
- 阶段收口时统一跑 `testDebugUnitTest`、`assembleDebug`
