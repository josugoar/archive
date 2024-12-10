#include <stdio.h>
#include "esp_wifi.h"
#include <wifi_provisioning/manager.h>
#include <wifi_provisioning/scheme_softap.h>


static esp_event_loop_handle_t event_loop_handle;



void app_main(void)
{
    esp_wifi_init();

    esp_event_loop_args_t event_loop_args = {
        .queue_size = CONFIG_ESP_SYSTEM_EVENT_QUEUE_SIZE,
        .task_name = "esp_event_loop_run_task",
        .task_priority = ESP_TASKD_EVENT_PRIO,
        .task_stack_size = ESP_TASKD_EVENT_STACK,
        .task_core_id = tskNO_AFFINITY,
    };

    esp_event_loop_create(&event_loop_args, &event_loop_handle);

    
    // 1: Provisionamiento
    
    // 2: Coneccion WiFi
    
    //2.2: Tarea Sensor
    

}
