package lucasnetwork.brainmagic.com.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {
        private CardView Registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Registered = (CardView)findViewById(R.id.first);
        Registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
