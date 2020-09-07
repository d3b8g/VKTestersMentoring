package net.d3b8g.vktestersmentoring.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.interfaces.LonggridCall
import net.d3b8g.vktestersmentoring.modules.LongGridModule
import net.d3b8g.vktestersmentoring.prefs.getQualityParam
import net.d3b8g.vktestersmentoring.prefs.getReadParam

class LongGridAdapter(val terf:LonggridCall): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val lG:ArrayList<LongGridModule> = ArrayList()

    fun update(ct:Context){
        val titleList = arrayListOf("Charles, Чарлик или Ваза, взгляд сквозь тело","DevTools — must have для тестирования сервисов","Уязвимости для новичков","Глоссарий","I want you for Special Forces","История одного террориста","Снимаем логи на Android","Качаем скилл создания хороших отчетов","Тестируем по правилам","Как избегать создания дубликатов","Как снять логи и крашлоги на iPhone","Через тернии к баллам")
        val linkList = arrayListOf("https://vk.com/@testpool-charles-charlik-ili-vaza-vzglyad-skvoz-telo","https://vk.com/@testpool-devtools-must-have-dlya-testirovaniya-servisov","https://vk.com/@testpool-uyazvimosti-dlya-novichkov","https://vk.com/@testpool-glossarii","https://vk.com/@testpool-i-want-you-for-special-forces","https://vk.com/@testpool-istoriya-horoshego-terrorista","https://vk.com/@testpool-snimaem-logi-na-android","https://vk.com/@testpool-kachaem-skilly-qa-cherez-otchety","https://vk.com/@testpool-testiruem-po-pravilam","https://vk.com/@testpool-dubli","https://vk.com/@testpool-ios-logs","https://vk.com/@testpool-cherez-ternii-k-ballam")
        for ( (index,title) in titleList.withIndex()) lG.add(LongGridModule(title = title, hadRead = getReadParam(ct, "check_box_$index"), quality = getQualityParam(ct, "quality_grid_$index"),link = linkList[index]))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_longrid, parent, false)
        return LonggridViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return lG.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LonggridViewHolder) holder.bind(lG[position])
    }

    inner class LonggridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.lg_name)
        fun bind(item:LongGridModule) {
            title.text = item.title
            itemView.setOnClickListener {
                terf.changeParam(item,position)
            }
        }
    }
}