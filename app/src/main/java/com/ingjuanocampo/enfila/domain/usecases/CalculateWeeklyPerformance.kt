package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.Shift
import java.util.Calendar
import javax.inject.Inject

class CalculateWeeklyPerformance
    @Inject
    constructor() {
        data class WeeklyData(
            val dailyShifts: List<Float>, // 7 days, Sunday to Saturday
            val dayLabels: List<String>,
            val totalWeekShifts: Int,
            val averagePerDay: Float,
            val peakDay: String,
        )

        operator fun invoke(shifts: List<Shift>): WeeklyData {
            val calendar = Calendar.getInstance()
            val currentWeekShifts = getCurrentWeekShifts(shifts, calendar)

            // Create array for 7 days (Sunday = 0 to Saturday = 6)
            val dailyShifts = FloatArray(7) { 0f }
            val dayLabels = getDayLabels()

            // Group shifts by day of week
            currentWeekShifts.forEach { shift ->
                val shiftCalendar =
                    Calendar.getInstance().apply {
                        timeInMillis = shift.date
                    }
                val dayOfWeek = shiftCalendar.get(Calendar.DAY_OF_WEEK) - 1 // Sunday = 0
                dailyShifts[dayOfWeek]++
            }

            val totalWeekShifts = currentWeekShifts.size
            val averagePerDay = if (totalWeekShifts > 0) totalWeekShifts.toFloat() / 7f else 0f
            val peakDay = findPeakDay(dailyShifts, dayLabels)

            return WeeklyData(
                dailyShifts = dailyShifts.toList(),
                dayLabels = dayLabels,
                totalWeekShifts = totalWeekShifts,
                averagePerDay = averagePerDay,
                peakDay = peakDay,
            )
        }

        private fun getCurrentWeekShifts(
            shifts: List<Shift>,
            calendar: Calendar,
        ): List<Shift> {
            // Get start of current week (Sunday)
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val weekStart = calendar.timeInMillis

            // Get end of current week (Saturday)
            calendar.add(Calendar.DAY_OF_WEEK, 6)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val weekEnd = calendar.timeInMillis

            return shifts.filter { shift ->
                shift.date in weekStart..weekEnd
            }
        }

        private fun getDayLabels(): List<String> {
            return listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        }

        private fun findPeakDay(
            dailyShifts: FloatArray,
            dayLabels: List<String>,
        ): String {
            val maxIndex = dailyShifts.indices.maxByOrNull { dailyShifts[it] } ?: 0
            val maxValue = dailyShifts[maxIndex]
            return if (maxValue > 0) {
                "${dayLabels[maxIndex]} (${maxValue.toInt()})"
            } else {
                "No data"
            }
        }

        /**
         * Get shifts for last N weeks for trend analysis
         */
        fun getWeeklyTrend(
            shifts: List<Shift>,
            weeksBack: Int = 4,
        ): List<WeeklyData> {
            val calendar = Calendar.getInstance()
            val weeklyData = mutableListOf<WeeklyData>()

            for (i in 0 until weeksBack) {
                val weekShifts = getWeekShifts(shifts, calendar, i)
                val dailyShifts = FloatArray(7) { 0f }

                weekShifts.forEach { shift ->
                    val shiftCalendar =
                        Calendar.getInstance().apply {
                            timeInMillis = shift.date
                        }
                    val dayOfWeek = shiftCalendar.get(Calendar.DAY_OF_WEEK) - 1
                    dailyShifts[dayOfWeek]++
                }

                weeklyData.add(
                    WeeklyData(
                        dailyShifts = dailyShifts.toList(),
                        dayLabels = getDayLabels(),
                        totalWeekShifts = weekShifts.size,
                        averagePerDay = weekShifts.size.toFloat() / 7f,
                        peakDay = findPeakDay(dailyShifts, getDayLabels()),
                    ),
                )
            }

            return weeklyData.reversed() // Return oldest to newest
        }

        private fun getWeekShifts(
            shifts: List<Shift>,
            calendar: Calendar,
            weeksBack: Int,
        ): List<Shift> {
            val tempCalendar = calendar.clone() as Calendar

            // Go back to the desired week
            tempCalendar.add(Calendar.WEEK_OF_YEAR, -weeksBack)

            // Get start of that week (Sunday)
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0)
            tempCalendar.set(Calendar.MINUTE, 0)
            tempCalendar.set(Calendar.SECOND, 0)
            tempCalendar.set(Calendar.MILLISECOND, 0)
            val weekStart = tempCalendar.timeInMillis

            // Get end of that week (Saturday)
            tempCalendar.add(Calendar.DAY_OF_WEEK, 6)
            tempCalendar.set(Calendar.HOUR_OF_DAY, 23)
            tempCalendar.set(Calendar.MINUTE, 59)
            tempCalendar.set(Calendar.SECOND, 59)
            tempCalendar.set(Calendar.MILLISECOND, 999)
            val weekEnd = tempCalendar.timeInMillis

            return shifts.filter { shift ->
                shift.date in weekStart..weekEnd
            }
        }
    }
