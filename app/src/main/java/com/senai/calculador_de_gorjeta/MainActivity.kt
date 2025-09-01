package com.senai.calculador_de_gorjeta

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senai.calculador_de_gorjeta.ui.theme.Calculador_de_gorjetaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculador_de_gorjetaTheme {
                Scaffold(
                    modifier = Modifier
                ) { innerPadding ->
                    CalculadorDeGorjetaApp(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculadoraComEntradaDeTexto(
    modifier: Modifier
) {
    var valorDaContaInput by remember { mutableStateOf("") }
    val valorConta = valorDaContaInput.toDoubleOrNull() ?: 0.0

    var porcentagemGorjetaInput by remember { mutableStateOf("") }
    val porcentagemGorjeta = porcentagemGorjetaInput.toDoubleOrNull() ?: 5.0
    var estadoBotao by remember { mutableStateOf(false) }

    val gorjeta = calcularGorjeta(valorConta, porcentagemGorjeta, estadoBotao)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.calcular_gorjeta),
            textAlign = TextAlign.Left,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoNumericoEditavel(
            textoLabel = R.string.valor_da_gorjeta_input,
            valor = valorDaContaInput,
            onValueChange = { valorDaContaInput = it },
            leadingIcon = R.drawable.money_icon)

        Spacer(modifier = Modifier.height(35.dp))

        CampoNumericoEditavel(
            textoLabel = R.string.porcentagem_gorjeta,
            valor = porcentagemGorjetaInput,
            onValueChange = { porcentagemGorjetaInput = it },
            leadingIcon = R.drawable.percent_icon)

        Spacer(modifier = Modifier.height(35.dp))

        SwitchArredondarGorjeta(onCheckedChange = { estadoBotao = it }, estadoBotao = estadoBotao)

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            stringResource(R.string.valor_da_gorjeta_text, gorjeta),
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculadorDeGorjetaApp(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
) {
    CalculadoraComEntradaDeTexto(modifier)
}

private fun calcularGorjeta(
    valorConta: Double,
    porcentagemGorjeta: Double = 15.0,
    arredondarGorjeta: Boolean = false
): String {
    var gorjeta = porcentagemGorjeta / 100 * valorConta

    if (arredondarGorjeta) {
        gorjeta = kotlin.math.ceil(gorjeta)
    }

    return NumberFormat.getCurrencyInstance().format(gorjeta)
}

@Composable
fun CampoNumericoEditavel(
    textoLabel: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onValueChange: (String) -> Unit,
    valor: String,
    leadingIcon: Int
) {
    TextField(
        label = { Text(stringResource(textoLabel)) },
        leadingIcon = { Icon(painterResource(leadingIcon), "") },
        value = valor,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun SwitchArredondarGorjeta(
    modifier: Modifier = Modifier,
    estadoBotao: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.arredondar_gorjeta), textAlign = TextAlign.Left)
        Switch(
            onCheckedChange = onCheckedChange,
            checked = estadoBotao,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}