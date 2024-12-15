package com.hete.supply.scm.server.scm.common.service.base;

/**
 * @author yanjiawei
 * @Description 容器接口定义了占用和释放容器的行为。不同的容器实现可以实现这些方法以提供特定的占用和释放逻辑。
 * @Date 2023/10/25 09:47
 */
public interface IContainer {
    /**
     * 尝试占用容器的方法。
     *
     * @return 如果容器占用成功，返回 true；否则返回 false。
     */
    boolean tryOccupyContainer();

    /**
     * 尝试释放容器的方法。
     *
     * @return 如果容器释放成功，返回 true；否则返回 false。
     */
    boolean tryReleaseContainer();

}
