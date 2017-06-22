package seteat.stripetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    private TextView cardNumber;
    private TextView month;
    private TextView year;
    private TextView cvc;
    private Button submitBtn;
    private TextView indicateLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardNumber = (TextView) findViewById(R.id.cardNumber);
        month = (TextView) findViewById(R.id.month);
        year = (TextView) findViewById(R.id.year);
        cvc = (TextView) findViewById(R.id.cvc);
        submitBtn = (Button) findViewById(R.id.submitButton);
        indicateLabel = (TextView) findViewById(R.id.indicateLabel);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCardInfo();
                indicateLabel.setText("Success!");
            }
        });

    }

    private void submitCardInfo() {
        Card card = new Card(
                cardNumber.getText().toString(),
                Integer.valueOf(month.getText().toString()),
                Integer.valueOf(year.getText().toString()),
                cvc.getText().toString()
        );
        if(!card.validateCard()) {
            indicateLabel.setText("Card Invalid");
        }

        Context mContext = this;

        Stripe stripe = new Stripe(mContext, "pk_test_qvzr6qG9nIaLtsX5IHaFglKt");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        chargeClient(token);
                    }
                    public void onError(Exception error) {
                        error.printStackTrace();
                    }
                }
        );
    }


    public void chargeClient(Token token) {
        // set secret key
        com.stripe.Stripe.apiKey = "sk_test_YRlSvwxgUo0ZPkSn3RVxYyp7";

        String tokId = token.getId();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", 10);
        params.put("currency", "usd");
        params.put("description", "Example charge from Android");
        params.put("source", tokId);

        try {
            Charge charge = Charge.create(params);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

    }





}
