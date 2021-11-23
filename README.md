# SquarePlatform


## Description and inspiration

Simple 2D scrolling game created with the purpose of learning
game development, and also sharing with other people who wants
to learn as well.


## Author and Status

Author: Fernando Giorgetti

Status: Under development


## Plans and purposes

A 2D scrolling game created for learning about 2D game development,
using nothing but Java SE.

Hope to complete this game and then I am planning to re-create it 
using some game development library like LWJGL or LibGDX. But that is 
a future plan.

For now, the purpose is to have a complete 2D scrolling game with a
few levels, music, enemies, a menu and a few more stuff.

## Installing OpenJfx runtime dependency

Download https://download2.gluonhq.com/openjfx/17.0.1/openjfx-17.0.1_linux-x64_bin-sdk.zip.
Uncompress the zip file and link it to the ./lib, example:

```
ln -s javafx-sdk-17.0.1/lib/ ./
```

## Building

```
mvn clean package
```

## Running the game

```
./square.sh
```

## Running the map editor

```
./mapeditor.sh
```
