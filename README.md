## Minimalistic example of Idea UI test

1. Specify Idea version or path to local Idea in `intellij` section of build.gradle.kts
2. Run gradle task `runIdeForUiTests`
3. Run gradle task `test`

```shell
 ./gradlew runIdeForUiTests & ./gradlew test
```