
package com.example.booking.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable


@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkTime(val start: String, val end: String)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkPeriod(val start: String, val end: String)

@OptIn(InternalSerializationApi::class)
@Serializable
data class BreakTime(val start: String, val end: String)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingHoursData(
    val scheduleType: String,
    val workTime: WorkTime,
    val period: WorkPeriod,
    val breaks: List<BreakTime>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingWeeksHoursData(
    val scheduleType: String,
    val dayOfWeek: String,
    val scheduleSubType: String,
    val workTime: WorkTime,
    val period: WorkPeriod,
    val breaks: List<BreakTime>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class BreakRequest(val startTime: String, val endTime: String)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkTimeSlotRequest(
    val startTime: String,
    val endTime: String,
    val validFrom: String,
    val validTo: String,
    val breaks: List<BreakRequest>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingHoursRequest(
    val employeeId: Int,
    val scheduleType: String,
    val workTimeSlots: List<WorkTimeSlotRequest>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingHoursResponse(
    val scheduleType: String,
    val workTimeSlots: List<WorkTimeSlotRequest>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingWeeksHoursRequest(
    val employeeId: Int,
    val scheduleType: String,
    val dayOfWeek: String,
    val scheduleSubType: String,
    val workTimeSlots: List<WorkTimeSlotRequest>
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class WorkingWeeksHoursResponse(
    val scheduleType: String,
    val dayOfWeek: String,
    val scheduleSubType: String,
    val workTimeSlots: List<WorkTimeSlotRequest>
)


