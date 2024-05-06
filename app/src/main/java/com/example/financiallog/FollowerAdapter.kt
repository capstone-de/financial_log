import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FollowerAdapter(context: Context, resource: Int, followers: List<String>) : ArrayAdapter<String>(context, resource, followers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = View.inflate(context, android.R.layout.simple_list_item_1, null)
        }

        val followerName = getItem(position)
        val textView = itemView as TextView
        textView.text = followerName

        return textView
    }
}
