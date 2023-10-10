package com.gcs.appantiroboandroid.presentation.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.gcs.appantiroboandroid.ConstantsUtils;
import com.gcs.appantiroboandroid.R;
import com.gcs.appantiroboandroid.model.AttributesModel;
import com.gcs.appantiroboandroid.repository.AttributesRepository;
import com.gcs.appantiroboandroid.service.ReportLocationService;

/**
 *
 * Fragmento de configuración de la aplicación.
 * Este fragmento se utiliza para mostrar y gestionar las preferencias y ajustes de la aplicación.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "APP_ANTIROBO - SettingsFragment";
    private final AttributesRepository attributesRepository;
    private ActivityResultLauncher<String> resultPermissionsAccessFineLocation;
    private ActivityResultLauncher<String> resultPermissionsAccessBackgroundLocation;
    private EditTextPreference editTextEmailMain;
    private EditTextPreference editTextName;
    private EditTextPreference editTextMailSecondary;
    private SwitchPreferenceCompat switchPreferenceTrackerBackground;
    private SwitchPreferenceCompat switchPreferenceTrackerGPS;
    private SwitchPreferenceCompat switchPreferenceTrackerGPSBackground;
    private ListPreference listPreferenceMinuteIterator;


    /**
     * Constructor de la clase SettingsFragment.
     *
     * @param attributesRepository Repositorio de atributos utilizado para el almacenamiento y recuperación de datos de configuración.
     */
    public SettingsFragment(final AttributesRepository attributesRepository) {
        Log.d(TAG, "SettingsFragment| builder");
        this.attributesRepository = attributesRepository;
    }

    /**
     * Este método obtiene valores de almacenamiento de atributos y los asigna a los campos de configuración en la pantalla.
     * Los valores recuperados incluyen el nombre de la cuenta, el correo principal, el correo secundario,
     * el intervalo de minutos y las preferencias para activar/desactivar el rastreo GPS y el rastreo en segundo plano.
     * Estos valores se utilizan para prellenar los campos de configuración y configurar los interruptores de acuerdo a las preferencias almacenadas.
     */
    private void getValuesStorage() {



        // Obtiene los valores de almacenamiento de atributos de la base de datos.
        AttributesModel accountNameAttr = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_NAME);
        AttributesModel accountEmailAttr = this.attributesRepository.getValue(ConstantsUtils.ACCOUNT_EMAIL);
        AttributesModel emailSecondaryAttr = this.attributesRepository.getValue(ConstantsUtils.EMAIL_SECONDARY);
        AttributesModel minutesIteratorAttr = this.attributesRepository.getValue(ConstantsUtils.MINUTES_ITERATOR);
        AttributesModel isActivateGPSAttr = this.attributesRepository.getValue(ConstantsUtils.IS_ACTIVATE_GPS);
        AttributesModel isActivateGPSBackgroundAttr = this.attributesRepository.getValue(ConstantsUtils.IS_ACTIVATE_GPS_BACKGROUND);
        AttributesModel isActivateTrackerAttr = this.attributesRepository.getValue(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND);
        Log.d(TAG, "getValuesStorage| Obteniendo valores de atributos de almacenamiento.");

        // Asigna los valores recuperados a los campos de configuración en la pantalla.
        // Si se recuperan valores válidos, prellenará los campos correspondientes y configurará los interruptores según las preferencias almacenadas.
        if (accountNameAttr != null) {
            String accountName = accountNameAttr.getValue();
            editTextName.setText(accountName);
            editTextName.setEnabled(false);
        }
        if (accountEmailAttr != null) {
            String accountEmail = accountEmailAttr.getValue();
            editTextEmailMain.setText(accountEmail);
            editTextEmailMain.setEnabled(false);
        }
        if (emailSecondaryAttr != null) {
            String emailSecondary = emailSecondaryAttr.getValue();
            editTextMailSecondary.setText(emailSecondary);
        }
        if (minutesIteratorAttr != null) {
            String minutesIterator = minutesIteratorAttr.getValue();
            listPreferenceMinuteIterator.setValue(minutesIterator);
        }
        if (isActivateGPSAttr != null) {
            String isActivateGPS = isActivateGPSAttr.getValue();
            switchPreferenceTrackerGPS.setChecked(("1".equals(isActivateGPS)));
        }
        if (isActivateGPSBackgroundAttr != null) {
            String isActivateGPSBackground = isActivateGPSBackgroundAttr.getValue();
            switchPreferenceTrackerGPSBackground.setChecked(("1".equals(isActivateGPSBackground)));
        }
        if (isActivateTrackerAttr != null) {
            String isActivateTracker = isActivateTrackerAttr.getValue();
            switchPreferenceTrackerBackground.setChecked(("1".equals(isActivateTracker)));
        }
    }





    /**
    * Este método se llama al crear las preferencias de la aplicación.
    * Inicializa las preferencias a partir de un archivo XML de recursos
    * y configura los campos y valores necesarios para la configuración.
    *
    * @param savedInstanceState El estado previamente guardado de la instancia de la actividad.
    * @param rootKey La clave de la preferencia raíz, si existe.
    */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Registra un mensaje de depuración para indicar que se está haciendo referencia al archivo XML de SettingsFragment.
        Log.d(TAG, "onCreatePreferences| Haciendo referencia al archivo XML con SettingsFragment.");
        // Configura las preferencias a partir del archivo XML de recursos especificado.
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    
        // Obtiene las referencias a los campos de configuración de la pantalla.
        // Estos campos incluyen el correo principal, el nombre, el correo secundario,
        // las preferencias de rastreo en segundo plano, rastreo GPS y el intervalo de minutos.
        
        // Registra mensajes de depuración para indicar que se están obteniendo estas referencias.
        Log.d(TAG, "onCreatePreferences| Obteniendo referencias a los campos de configuración.");
        editTextEmailMain = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_EMAIL_ACCOUNT));
        editTextName = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_NAME_ACCOUNT));
        editTextMailSecondary = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_EMAIL_SECONDARY));
        switchPreferenceTrackerBackground = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_ACTIVATE_TRACKER_BACKGROUND));
        switchPreferenceTrackerGPS = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_TRACKER_GPS));
        switchPreferenceTrackerGPSBackground = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_TRACKER_GPS_BACKGROUND));
        listPreferenceMinuteIterator = findPreference(getString(R.string.SETTINGS_ACTIVITY_KEY_FIELD_TIME_SEND));
    
        // Obtiene las credenciales de Google Storage de la aplicación.
        // Estas credenciales se utilizan para acceder a datos almacenados en la nube.
        
        // Registra un mensaje de depuración para indicar que se están obteniendo estas credenciales.
        Log.d(TAG, "onCreatePreferences| Obteniendo las credenciales de almacenamiento de Google de la aplicación.");
        getValuesStorage();
    
        // Carga los métodos para validar el resultado de las solicitudes de permisos.
        // Estos métodos se utilizan para gestionar los permisos de la aplicación.
        
        // Registra un mensaje de depuración para indicar que se están cargando estos métodos.
        Log.d(TAG, "onCreatePreferences| Cargando métodos para validar el resultado de las solicitudes de permisos.");
        loadsMethodsValidateResultPermissions();
    
        // Carga los métodos para modificar la configuración de la aplicación.
        // Estos métodos se utilizan para aplicar cambios en las preferencias.
        
        // Registra un mensaje de depuración para indicar que se están cargando estos métodos.
        Log.d(TAG, "onCreatePreferences| Cargando métodos para modificar la configuración.");
        loadsEventsOnPreferenceChange();
    }






    /**
     * Este método configura y registra oyentes para detectar cambios en las preferencias de la aplicación.
     * Cuando se cambian ciertas preferencias, este método realiza acciones específicas,
     * como guardar datos en un repositorio, iniciar servicios o mostrar mensajes de usuario.
     * Las preferencias monitoreadas incluyen el correo secundario, el rastreo GPS en segundo plano,
     * el rastreo GPS y ejecuccion en segundo plano.
     * Además, se registra cualquier cambio en la preferencia del intervalo de minutos.
     */
    public void loadsEventsOnPreferenceChange() {

        // Configura un oyente para el cambio de la preferencia editTextMailSecondary.
        // Registra y guarda el nuevo correo secundario en el repositorio de atributos.
        Log.d(TAG, "loadsEventsOnPreferenceChange| load method to change the value of object editTextMailSecondary on click ");
        editTextMailSecondary.setOnPreferenceChangeListener((preference, newValue) -> {
            Log.d(TAG, "loadsEventsOnPreferenceChange| editTextMailSecondary -> new email secondary = " + newValue);
            try {
                Log.d(TAG, "loadsEventsOnPreferenceChange| editTextMailSecondary -> save email secondary = " + newValue);
                attributesRepository.saveAttribute(ConstantsUtils.EMAIL_SECONDARY, newValue.toString());
            } catch (RuntimeException runtimeException) {
                Log.e(TAG, "loadsEventsOnPreferenceChange| editTextMailSecondary -> Error: SQLException message =" + runtimeException.getMessage());
                return false;
            }
            return true;
        });

        // Configura un oyente para el cambio de la preferencia switchPreferenceTrackerGPSBackground.
        // Realiza acciones según el estado del interruptor y el nuevo valor.
        // Puede iniciar solicitudes de permisos de ubicación en segundo plano.
        Log.d(TAG, "loadsEventsOnPreferenceChange| load method to change the value of object switchPreferenceTrackerGPSBackground on click ");
        switchPreferenceTrackerGPSBackground.setOnPreferenceChangeListener((preference, newValue) -> {

            Boolean valueEvent = (Boolean) newValue;
            Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> switch = " + newValue.toString());

            SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;
            if (!switchPreferenceCompat.isChecked() && valueEvent) {
                Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> switch tracker GPS Background OFF and newValue is ON");
                Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> validate switch tracker GPS is ON");
                if (this.switchPreferenceTrackerGPS.isChecked()) {
                    Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> switch tracker GPS ON");
                    Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> invoke method SettingsFragment.launchRequestsPermissionsLocationBackground");
                    launchRequestsPermissionsLocationBackground();
                } else {
                    Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> switch tracker GPS OFF");
                    Toast.makeText(getContext(), "Debe estar activado opcion GPS ", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (switchPreferenceCompat.isChecked() && !valueEvent) {
                Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPSBackground -> switch tracker GPS Background ON and newValue is OFF");
                switchPreferenceCompat.setChecked(false);
                return false;
            }
            return false;
        });

        // Configura un oyente para el cambio de la preferencia switchPreferenceTrackerGPS.
        // Realiza acciones según el estado del interruptor y el nuevo valor.
        // Puede iniciar solicitudes de permisos de ubicación.
        Log.d(TAG, "loadsEventsOnPreferenceChange| load method to change the value of object switchPreferenceTrackerGPS on click ");
        switchPreferenceTrackerGPS.setOnPreferenceChangeListener((preference, newValue) -> {

            Boolean valueEvent = (Boolean) newValue;
            Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerGPS -> switch = " + newValue.toString());

            SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;
            if (switchPreferenceCompat.isChecked() && (!valueEvent)) {

                Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerGPS -> switch tracker GPS ON and newValue is OFF");
                switchPreferenceCompat.setChecked(false);
                return false;
            } else {

                if ((!switchPreferenceCompat.isChecked()) && (valueEvent)) {

                    Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPS -> switch tracker GPS OFF and newValue is ON");
                    Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerGPS -> invoke method SettingsFragment.launchRequestsPermissionsLocation");
                    launchRequestsPermissionsLocation();
                }
            }
            return false;
        });

        // Configura un oyente para el cambio de la preferencia switchPreferenceTrackerBackground.
        // Inicia o detiene un servicio de seguimiento de ubicación en función del estado del interruptor y el nuevo valor.
        Log.d(TAG, "loadsEventsOnPreferenceChange| load method to change the value of object switchPreferenceTrackerBackground on click ");
        switchPreferenceTrackerBackground.setOnPreferenceChangeListener((preference, newValue) -> {

            Boolean valueEvent = (Boolean) newValue;
            Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> switch = " + newValue.toString());

            Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> create intent for launch operation for Service LocationRepostService ");
            Intent intent = new Intent(this.getContext(), ReportLocationService.class);
            SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;

            if ((!switchPreferenceCompat.isChecked()) && (valueEvent)) {
                Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> switch tracker background OFF and newValue is ON");
                Log.d(TAG,"loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> invoke method Context.startService for launch service LocationRepostService");


                // Valida que existan los permisos de GPS, GPS_BACKGROUND y MINUTES ITERATOR
                if(!switchPreferenceTrackerGPS.isChecked()){
                    attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_OFF);
                    switchPreferenceCompat.setChecked(false);
                    this.requireContext().stopService(intent);
                    Toast.makeText(getContext(), "Debe Tener permiso GPS Activado", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }
                if(!switchPreferenceTrackerGPSBackground.isChecked()){
                    attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_OFF);
                    switchPreferenceCompat.setChecked(false);
                    this.requireContext().stopService(intent);
                    Toast.makeText(getContext(), "Debe Tener permiso GPS Segundo plano Activado", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }
                if(listPreferenceMinuteIterator.getValue()==null){
                    attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_OFF);
                    switchPreferenceCompat.setChecked(false);
                    Toast.makeText(getContext(), "Debe Tener estar estar asignado \"Iteracion de Envio\" ", Toast.LENGTH_LONG)
                            .show();
                    this.requireContext().stopService(intent);
                    return false;
                }/*
                if(listPreferenceMinuteIterator.getValue().isEmpty()){
                    attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_OFF);
                    switchPreferenceCompat.setChecked(false);
                    this.requireContext().stopService(intent);
                    return false;
                }*/
                this.requireContext().startService(intent);
                attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_ON);
                switchPreferenceCompat.setChecked(true);
                return true;
            }
            if ((switchPreferenceCompat.isChecked()) && (!valueEvent)) {
                Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> switch tracker background ON and newValue is OFF");
                Log.d(TAG, "loadsEventsOnPreferenceChange| switchPreferenceTrackerBackground -> invoke method Context.stopService for stop service LocationRepostService");
                this.requireContext().stopService(intent);
                switchPreferenceCompat.setChecked(false);
                attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_TRACKER_BACKGROUND, ConstantsUtils.STATUS_OFF);
                return false;
            }
            return false;

        });


        // Configura un oyente para el cambio de la preferencia listPreferenceMinuteIterator.
        // Registra cualquier cambio en la preferencia del intervalo de minutos.
        Log.d(TAG, "loadsEventsOnPreferenceChange| load method to change the value of object listPreferenceMinuteIterator on click ");
        listPreferenceMinuteIterator.setOnPreferenceChangeListener(((preference, newValue) -> {
            Log.d(TAG, "loadsEventsOnPreferenceChange| listPreferenceMinuteIterator -> newValue" + newValue.toString());
            attributesRepository.saveAttribute(ConstantsUtils.MINUTES_ITERATOR, newValue.toString());
            return true;
        }));

    }






    /**
     * Este método se utiliza para cargar y configurar los resultados de las solicitudes de permisos.
     * Se registran dos resultados de actividad para permisos: uno para ACCESS_FINE_LOCATION y otro para ACCESS_BACKGROUND_LOCATION.
     * Cuando se completa una solicitud de permisos, se realizan acciones según si se otorgaron o no los permisos.
     */
    private void loadsMethodsValidateResultPermissions() {
        

        // Registrar un resultado de actividad para la solicitud de permiso ACCESS_FINE_LOCATION
        this.resultPermissionsAccessFineLocation = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
                    if (isGranted) {
                        attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS, ConstantsUtils.STATUS_ON);
                        this.switchPreferenceTrackerGPS.setChecked(true);
                        Toast.makeText(getContext(), "Permiso exitoso GPS ACCESS_FINE_LOCATION", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        this.switchPreferenceTrackerGPS.setChecked(false);
                        attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS, ConstantsUtils.STATUS_OFF);
                        Toast.makeText(getContext(), "Error al activar el GPS ACCESS_FINE_LOCATION", Toast.LENGTH_LONG)
                                .show();
                    }
                });


        // Registrar un resultado de actividad para la solicitud de permiso ACCESS_BACKGROUND_LOCATION
        this.resultPermissionsAccessBackgroundLocation = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        this.switchPreferenceTrackerGPSBackground.setChecked(true);
                        attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS_BACKGROUND, ConstantsUtils.STATUS_ON);
                        Toast.makeText(getContext(), "Permiso exitoso GPS ACCESS_BACKGROUND_LOCATION", Toast.LENGTH_LONG).show();
                    } else {
                        this.switchPreferenceTrackerGPSBackground.setChecked(false);
                        attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS_BACKGROUND, ConstantsUtils.STATUS_OFF);
                        Toast.makeText(getContext(), "Error al activar el GPS ACCESS_BACKGROUND_LOCATION",Toast.LENGTH_LONG).show();
                    }
                });
    }






    /**
     * Este método se utiliza para solicitar permisos de ubicación precisa (ACCESS_FINE_LOCATION)
     * y controlar el estado de la preferencia de seguimiento de GPS.
     */
    public void launchRequestsPermissionsLocation() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            resultPermissionsAccessFineLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            this.switchPreferenceTrackerGPS.setChecked(true);
            attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS, ConstantsUtils.STATUS_ON);
        }
    }






    /**
     * Este método se utiliza para solicitar permisos de ubicación en segundo plano
     * y controlar el estado de la preferencia de seguimiento de GPS en segundo plano.
     */
    public void launchRequestsPermissionsLocationBackground() {
        if (ContextCompat.checkSelfPermission(this.requireContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            resultPermissionsAccessBackgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            this.switchPreferenceTrackerGPSBackground.setChecked(true);
            attributesRepository.saveAttribute(ConstantsUtils.IS_ACTIVATE_GPS_BACKGROUND, ConstantsUtils.STATUS_ON);
        }
    }
}
