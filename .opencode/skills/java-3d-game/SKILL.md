---
name: java-3d-game
description: Maakt een klein 3D-spelletje in Java (JavaFX 3D, Maven) waarin een blokpoppetje met de pijltjestoetsen over een 3D-raster loopt. Gebruik bij verzoeken om dit spel te maken, uit te breiden of te bouwen.
---

# Java 3D spelletje (JavaFX + Maven)

## Doel
Een klein 3D-spel: een blokpoppetje loopt over een 3D-raster. Een volgcamera kijkt van achter en boven mee.

## Besturing
- Pijl omhoog: vooruit lopen (in kijkrichting van het poppetje)
- Pijl omlaag: achteruit lopen
- Pijl links: naar links draaien
- Pijl rechts: naar rechts draaien
- Spatiebalk: springen (alleen vanaf de grond; ingedrukt houden springt steeds opnieuw)
- ESC: pauzemenu openen/sluiten (Hervatten, Opnieuw beginnen, Afsluiten)

## Techniek
- Java 21+ (JavaFX 25 vraagt feitelijk 23+), JavaFX 3D (`javafx-controls`, `javafx-graphics`), Maven.
- Starten met `mvn javafx:run` (javafx-maven-plugin), bouwen met `mvn clean package`.
- Mainklasse `nl.spel.Main` extends `Application`.

## Projectstructuur
```
pom.xml
src/main/java/nl/spel/Main.java    # venster, SubScene, camera, gameloop, toetsen
src/main/java/nl/spel/Player.java  # blokpoppetje, positie, draaihoek, loopanimatie, sprong, reset
src/main/java/nl/spel/Grid.java    # tegelvloer met rasterlijnen
src/main/java/nl/spel/PauseMenu.java # pauze-overlay met drie knoppen
```

## Ontwerpregels
- Poppetje: `Group` van `Box`-onderdelen (hoofd, lijf, 2 armen, 2 benen) met `PhongMaterial`.
- Raster: vlak van tegels met afwisselende kleuren; grootte staat in `Main.GRID_SIZE`.
- Let op: in JavaFX wijst +Y omlaag; de vloer ligt op y=0 en het poppetje staat op negatieve y.
- Scene: `SubScene` met `depthBuffer=true` en `SceneAntialiasing.BALANCED`, plus `AmbientLight` en `PointLight`.
- Camera: `PerspectiveCamera(true)` met een transformatieketen `Translate` (positie van het poppetje), `Rotate` (yaw = draaihoek poppetje), `Rotate` (pitch, negatieve hoek = naar beneden kijken) en `Translate` (afstand achter het poppetje). Elke frame bijgewerkt in de gameloop.
- Gameloop: `AnimationTimer`, beweging met delta-time. Snelheden: `Main.WALK_SPEED` en `Main.TURN_SPEED`.
- Toetsstatus bijhouden in een `Set<KeyCode>` via `scene.addEventFilter` (KEY_PRESSED/KEY_RELEASED, event consumeren zodat pijltjes de focus niet verplaatsen); zo werken meerdere toetsen tegelijk.
- Positie begrenzen tot het raster (`Player.clamp`).
- Sprong: `Player.jump` zet een opwaartse snelheid (`Player.JUMP_SPEED`) als het poppetje op de grond staat; `Player.update` past `Player.GRAVITY` toe en zet `translateY` op `-hoogte`. De camera springt niet mee. Loopanimatie staat stil in de lucht.
- Pauze: ESC schakelt `Main.paused` om en toont/verbergt `PauseMenu` (overlay in de `StackPane`). De gameloop slaat updates over tijdens pauze (`last` blijft bijgewerkt, geen dt-sprong). Tijdens pauze laat de toetsfilter alles door behalve ESC, zodat knoppen werken. `Player.reset` zet het poppetje terug op de start.
- Loopanimatie: armen en benen zwaaien met een sinus zolang het poppetje beweegt (`Player.update`).

## Synchronisatie skill en code
1. Wijzigt gedrag, besturing, projectstructuur of bouwcommando's, werk dan in dezelfde taak `SKILL.md` bij.
2. Staat er in de skill iets dat de code niet doet, pas dan ook de code aan.
3. Neem geen getallen of waarden over die in de code staan; verwijs naar de constante.
4. Sluit elke wijziging af met de stappen onder Verificatie.
5. Zie je een afwijking tussen skill en code, meld dan welke van de twee je hebt aangepast.

## Verificatie
1. `mvn -q clean package` moet slagen.
2. Handmatig: `mvn javafx:run`, controleer alle vier de pijltjes en de randbegrenzing.
3. Controleer springen (ook tijdens lopen, geen sprong in de lucht).
4. Controleer ESC-menu: Hervatten, Opnieuw beginnen, Afsluiten, en dat het spel stil staat tijdens pauze.
