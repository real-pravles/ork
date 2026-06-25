# ork (Org Mode Zettelkasten graph renderer)

## How to build

1. Run `mvn clean package`.
2. The file `ork-<Version>.jar` will be in the `target` directory.
3. Put the resulting binary into `~/sw/todos`.
4. Launch with a shell script like this:

```
java -jar todos-0.1-SNAPSHOT.jar <zettelkasten.org>
```