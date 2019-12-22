package ru.phoenix.game.logic.movement;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.math.Vector4f;
import ru.phoenix.game.logic.element.grid.Cell;

public class MousePicker {
    private static final int RECURSION_COUNT = 40;
    private static final float RAY_RANGE = 120;

    private Vector3f currentRay;
    private Vector3f currentTerrainPoint;

    private Cell[][] grid;

    public MousePicker() {
        currentRay = new Vector3f();
        currentTerrainPoint = new Vector3f();
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update(Cell[][] grid) {
        this.grid = grid; // записываем данные сетки
        currentRay = calculateMouseRay(); // собираем луч
        currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
    }

    private Vector3f calculateMouseRay() {
        float mouseX = Input.getInstance().getCursorPosition().getX(); // списываем положение X курсора на экране
        float mouseY = Input.getInstance().getCursorPosition().getY(); // списываем положение Y курсора на экране
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY); // собираем положение курсора в пространстве экрана
        /* Немного об однородных координатах в пространстве NDC. Собственно мы хотим спроэцировать луч на дальнюю стенку NDC куба. Для этого требуеться
        *  указать положение мыши в этом пространстве, спроэцировать указанные координаты на дальнюю стенку куба и что бы коорденаты получили необходимые
        *  изменения указываем 1 в w координату для дальнейших ее трансформации в мировой матрице*/
        Vector4f clipCoords = new Vector4f(normalizedCoords.getX(), -normalizedCoords.getY(), -1.0f, 1.0f); // собираем вектор NDC на дальнюю стенку куба
        Vector4f eyeCoords = toEyeCoords(clipCoords); // перестраиваем NDC вектор в вектор с учетом перспективы (конкретно заданной игрой)
        return toWorldCoords(eyeCoords); // домножаем вектор на обратную матрицу камеры и получаем конечый вектор луча
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f(Camera.getInstance().getPerspective().getViewMatrix()).invert(); // для правельного направления луча требуеться обратная матрица вида
        Vector4f rayWorld = new Vector4f(invertedView.mulOnVector(eyeCoords)); // умножаем обратную матрицу вида на вектор перспективы
        Vector3f mouseRay = new Vector3f(rayWorld.getX(), rayWorld.getY(), rayWorld.getZ()); // собираем луч из 4f в 3f вектор
        mouseRay.normalize(); // нормализуем
        return mouseRay; // возвращаем результат
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f(Camera.getInstance().getPerspective().getProjection()).invert(); // для правельного направления луча требуеться обратная матрица проекции
        Vector4f eyeCoords = new Vector4f(invertedProjection.mulOnVector(clipCoords)); // умножаем обратную матрицу проекции на NDC вектор и получаем вектор в перспективе
        return new Vector4f(eyeCoords.getX(), eyeCoords.getY(), -1f, 0f); // собираем полученный вектор и возвращаем его
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        /* Процедура следующая. Так как нормализованное пространство экрана это куб образованный от нуля в его центре и каждая сторона описывается
        *  от -1 до 1 следовательно необходимо перевести координаты курсора сначала в еденичный отрезок а после в пространство от -1 до 1
        *  пример: предположим что координата X мыши ровна 344 а разрешение экрана по горезонтали 1280 соотвественно для ее перевода в нормализованное
        *  пространство экрана требуеться разделить X / Width * 2 - 1 = -0.4625f*/
        float x = (2.0f * mouseX) / Window.getInstance().getWidth() - 1f;
        float y = (2.0f * mouseY) / Window.getInstance().getHeight() - 1f;
        return new Vector2f(x, y);
    }

    //**********************************************************

    private Vector3f getPointOnRay(Vector3f ray, float distance) { // функция получения точки на луче (описанна вектором луча и дистанцией на луче
        Vector3f camPos = new Vector3f(Camera.getInstance().getPos()); // получаем позицию камеры
        Vector3f start = new Vector3f(camPos.getX(), camPos.getY(), camPos.getZ()); // дублируем положение камеры в отдельный вектор
        Vector3f scaledRay = new Vector3f(ray.getX() * distance, ray.getY() * distance, ray.getZ() * distance); // масштабируем вектор луча
        return start.add(scaledRay); // возвращаем точку
    }

    private Vector3f binarySearch(float count, float start, float finish, Vector3f ray) { // currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        float section = start + count;
        if(intersectionInRange(start, section, ray)){
            Vector3f endPoint = getPointOnRay(ray,section);
            int x = Math.round(endPoint.getX()); if(x < 0) x = 0; if(x > grid.length - 1) x = grid.length - 1;
            int z = Math.round(endPoint.getZ()); if(z < 0) z = 0; if(z > grid[0].length - 1) z = grid[0].length - 1;
            return endPoint;
        }else{
            return binarySearch(count + 0.5f, start, finish, ray);
        }


        /*float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            int x = Math.round(endPoint.getX()); if(x < 0) x = 0; if(x > grid.length - 1) x = grid.length - 1;
            int z = Math.round(endPoint.getZ()); if(z < 0) z = 0; if(z > grid[0].length - 1) z = grid[0].length - 1;
            return endPoint;
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }*/
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) { // функция поиска клетки описанна начало, концом и вектором луча
        Vector3f startPoint = getPointOnRay(ray, start); // находим начальную точку
        Vector3f endPoint = getPointOnRay(ray, finish); // находим финальную точку
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f testPoint) { // проверка точки на положение в пространстве в отношении иследуемой поверхности
        boolean test = false;
        int x = Math.round(testPoint.getX()); if(x < 0) x = 0; if(x > grid.length - 1) x = grid.length - 1; // переводим точку к целочисленному значению
        int z = Math.round(testPoint.getZ()); if(z < 0) z = 0; if(z > grid[0].length - 1) z = grid[0].length - 1; // и проверяем на выход за пределы карты (если вышла то возвращаем в пределы)
        Cell cellPoint = grid[x][z]; // находим клетку
        float surfaceHeight = cellPoint.getPosition().getY(); // высота поверхности
        float currentSurfaceHeight = cellPoint.getCurrentHeight(); // текущая высота поверхности

        if(surfaceHeight == currentSurfaceHeight){
            test = testPoint.getY() < surfaceHeight;
        }else{
            boolean test1 = false;
            boolean test2 = false;
            test1 = testPoint.getY() < surfaceHeight;
            test2 = testPoint.getY() < currentSurfaceHeight;
            if(test1 || test2){
                test = true;
            }
        }

        return test;
    }
}
