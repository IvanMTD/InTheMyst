#!/usr/bin/env sh
# Запуск из папки игры, чтобы data/ и game.log нашлись независимо от текущей директории
cd "$(dirname "$0")"
exec java -Xms512m -Xmx2g -Dfile.encoding=UTF-8 -jar InTheMyst-all.jar "$@"
