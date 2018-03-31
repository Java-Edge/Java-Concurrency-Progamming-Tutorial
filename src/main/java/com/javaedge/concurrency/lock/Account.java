package com.javaedge.concurrency.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author JavaEdge
 * @date 2021/4/21
 */
class Account {

    private int balance;

    private final Lock lock = new ReentrantLock();

    /**
     * 转账
     *
     * @param target 目标账户
     * @param amount 转账金额
     */
    void errorTransfer(Account target, int amount) {
        while (true) {
            if (this.lock.tryLock()) {
                try {
                    if (target.lock.tryLock()) {
                        try {
                            this.balance -= amount;
                            target.balance += amount;
                        } finally {
                            target.lock.unlock();
                        }
                    } else {
                        // nothing
                    }
                } finally {
                    this.lock.unlock();
                }
            } else {
                // nothing
            }
        }
    }

    void correctTransfer(Account tar, int amt) throws InterruptedException {
        while (true) {
            if (this.lock.tryLock(new Random().nextLong(), TimeUnit.NANOSECONDS)) {
                try {
                    if (tar.lock.tryLock(new Random().nextLong(), TimeUnit.NANOSECONDS)) {
                        try {
                            this.balance -= amt;
                            tar.balance += amt;
                            break;
                        } finally {
                            tar.lock.unlock();
                        }
                    } else {
                        // nothing
                    }
                } finally {
                    this.lock.unlock();
                }
            } else {
                // nothing
            }
        }
    }
}
