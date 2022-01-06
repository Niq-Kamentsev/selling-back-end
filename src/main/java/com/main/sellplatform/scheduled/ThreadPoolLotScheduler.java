package com.main.sellplatform.scheduled;

import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@Component
public class ThreadPoolLotScheduler {

    private final LotDao2 lotDao2;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final PeriodicTrigger periodicTrigger;
    private final CronTrigger cronTrigger;

    @Autowired
    public ThreadPoolLotScheduler(ThreadPoolTaskScheduler taskScheduler, LotDao2 lotDao2, PeriodicTrigger periodicTrigger, CronTrigger cronTrigger) {
        this.taskScheduler = taskScheduler;
        this.lotDao2 = lotDao2;
        this.periodicTrigger = periodicTrigger;
        this.cronTrigger = cronTrigger;
    }

    @PostConstruct
    public void test(){
        taskScheduler.schedule(new RunnableTask(),cronTrigger);
    }

    class RunnableTask implements Runnable {
        @Override
        public void run() {
            List<Lot> allLots = lotDao2.getAllLots();
            allLots.stream().filter(lot ->
                    lot.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.now())

            ).forEach(lot -> {
                lot.setStatus(LotStatus.BIDDED);
                lotDao2.saveLot(lot);
            });
        }
    }

}
