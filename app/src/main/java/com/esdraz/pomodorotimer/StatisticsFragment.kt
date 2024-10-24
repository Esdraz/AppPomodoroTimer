package com.esdraz.pomodorotimer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class StatisticsFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Inicializar os gráficos
        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)

        setupPieChart()
        setupBarChart()

        return view
    }

    private fun setupPieChart() {
        // Exemplo de dados para os Pomodoros completados por dia da semana
        val pomodorosPorDia = mapOf(
            "Segunda" to 5f,
            "Terça" to 3f,
            "Quarta" to 4f,
            "Quinta" to 6f,
            "Sexta" to 2f,
            "Sábado" to 7f,
            "Domingo" to 3f
        )

        // Criar entradas para o gráfico de pizza
        val entries = pomodorosPorDia.map { PieEntry(it.value, it.key) }

        val dataSet = PieDataSet(entries, "Pomodoros por Dia")
        dataSet.colors = listOf(
            Color.parseColor("#FF8C00"), // Laranja
            Color.parseColor("#FFD700"), // Amarelo
            Color.parseColor("#FF4500"), // Vermelho
            Color.parseColor("#32CD32"), // Verde
            Color.parseColor("#4682B4"), // Azul
            Color.parseColor("#8A2BE2"), // Roxo
            Color.parseColor("#FF69B4")  // Rosa
        )

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Pomodoros Semanais"
        pieChart.animateY(1000)
    }

    private fun setupBarChart() {
        // Exemplo de dados para os Pomodoros completados por dia da semana
        val pomodorosPorDia = listOf(
            BarEntry(1f, 5f), // Segunda
            BarEntry(2f, 3f), // Terça
            BarEntry(3f, 4f), // Quarta
            BarEntry(4f, 6f), // Quinta
            BarEntry(5f, 2f), // Sexta
            BarEntry(6f, 7f), // Sábado
            BarEntry(7f, 3f)  // Domingo
        )

        val dataSet = BarDataSet(pomodorosPorDia, "Pomodoros por Dia")
        dataSet.color = Color.parseColor("#4682B4") // Azul

        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)
    }
}