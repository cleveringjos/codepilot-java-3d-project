# codepilot-java-3d-project

Een klein 3D-spel in Java, gemaakt met JavaFX 3D en Maven. Een blokpoppetje loopt over een 20x20 dambordvloer. Een volgcamera kijkt van achteren en van boven mee.

## Vereisten

- JDK 21 of hoger. JavaFX 25 vraagt in de praktijk JDK 23 of hoger, dus gebruik bij voorkeur JDK 23+.
- Apache Maven 3.9 of hoger.

JavaFX hoef je niet apart te installeren, Maven haalt het binnen.

## Het spel starten

```bash
mvn javafx:run
```

Alleen bouwen kan met:

```bash
mvn clean package
```

Let op: het jar in `target/` bevat geen JavaFX. `java -jar` werkt daarom niet zonder extra module-path. Start het spel dus via `mvn javafx:run`.

## Besturing

| Toets | Actie |
|---|---|
| Pijl omhoog | Vooruit lopen |
| Pijl omlaag | Achteruit lopen |
| Pijl links / rechts | Draaien |
| Spatiebalk | Springen (alleen vanaf de grond) |
| ESC | Pauzemenu openen of sluiten |

Het pauzemenu heeft de knoppen Hervatten, Opnieuw beginnen en Afsluiten.

## Features

- Blokpoppetje met hoofd, lijf, armen en benen.
- Loopanimatie: armen en benen zwaaien tijdens het lopen.
- Springen met zwaartekracht.
- Volgcamera achter en boven het poppetje.
- Dambordvloer van 20x20 tegels. Het poppetje blijft binnen het raster.
- Pauzemenu.
- Schaalbaar venster (start op 900x600).

## Projectstructuur

```
pom.xml                          Maven-configuratie (Java, JavaFX 25)
src/main/java/nl/spel/
  Main.java                      Venster, camera, gameloop en toetsafhandeling
  Player.java                    Het blokpoppetje: positie, draaien, animatie, springen
  Grid.java                      De dambordvloer
  PauseMenu.java                 Het pauzemenu
.opencode/skills/java-3d-game/   Opencode-skill die het spel beschrijft
```
