/*
  ==============================================================================

    NativeAndroidJuceActivity.cpp
    Created: 16 Dec 2020 12:43:46pm
    Author:  Adam Wilson

  ==============================================================================
*/

#define JUCE_CORE_INCLUDE_JNI_HELPERS 1

#include "../JuceLibraryCode/JuceHeader.h"
#include "MainComponent.h"

// the C++ counterpart class to the java class with the same name
class NativeAndroidJuceActivity
{
public:
    explicit NativeAndroidJuceActivity (jobject javaObject)
    {
        auto* env = getEnv();

        javaCounterpartInstance = env->NewWeakGlobalRef(javaObject);
        env->SetLongField (javaObject, NativeAndroidJuceActivityJavaClass.cppCounterpartInstance,
                           reinterpret_cast<jlong> (this));

        juceMainComponent.setVisible (true);
        juceMainComponent.showButton.onClick = [this]
        {
            showButtonClicked();
        };

        // initialise the JUCE message manager!
        MessageManager::getInstance();
    }

    ~NativeAndroidJuceActivity()
    {
        auto* env = getEnv();

        {
            LocalRef<jobject> javaThis (env->NewLocalRef (javaCounterpartInstance));

            if (javaThis != nullptr)
                env->SetLongField (javaThis.get(), NativeAndroidJuceActivityJavaClass.cppCounterpartInstance, 0);
        }

        env->DeleteWeakGlobalRef(javaCounterpartInstance);
    }

    void addJuceMainComponent (jobject containerView)
    {
        juceMainComponent.addToDesktop(0, containerView);
    }

private:
    void showButtonClicked()
    {
        auto* env = getEnv();

        LocalRef<jobject> javaThis (env->NewLocalRef (javaCounterpartInstance));

        if (javaThis != nullptr)
        {
            jclass arrayListClass = env->FindClass("java/util/ArrayList");
            jmethodID arrayClassConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
            jobject arrayList = env->NewObject(arrayListClass, arrayClassConstructor);
            jmethodID arrayClassAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

            for(int i = 0; i < 50; i++)
            {
                env->CallBooleanMethod(arrayList, arrayClassAdd, javaString (String ("Item : ") + String(i)).get());
            }

            env->CallVoidMethod (javaThis.get(), NativeAndroidJuceActivityJavaClass.showBottomSheet, arrayList);
        }
    }

    //==============================================================================
#define JNI_CLASS_MEMBERS(METHOD, STATICMETHOD, FIELD, STATICFIELD, CALLBACK) \
     METHOD   (showBottomSheet,           "showBottomSheet",        "(Ljava/util/ArrayList;)V") \
     FIELD    (cppCounterpartInstance,    "cppCounterpartInstance", "J") \
     CALLBACK (constructNativeClassJni,   "constructNativeClass",   "()V") \
     CALLBACK (destroyNativeClassJni,     "destroyNativeClass",     "()V") \
     CALLBACK (addJuceMainComponentJni,   "addJuceMainComponent",   "(Landroid/view/View;)V")

    DECLARE_JNI_CLASS (NativeAndroidJuceActivityJavaClass, "com/yourcompany/androidnativescrollview/NativeAndroidJuceActivity")
#undef JNI_CLASS_MEMBERS

    //==============================================================================
    // simple glue wrappers to invoke the native code
    static void JNIEXPORT constructNativeClassJni (JNIEnv* env, jobject javaInstance)
    {
        initialiseJuce_GUI();
        new NativeAndroidJuceActivity (javaInstance);
    }

    static void JNIEXPORT destroyNativeClassJni (JNIEnv* env, jobject javaInstance)
    {
        if (auto* myself = getCppInstance (env, javaInstance))
            delete myself;
    }

    static void JNIEXPORT addJuceMainComponentJni (JNIEnv* env, jobject javaInstance,
                                                     jobject viewContainer)
    {
        if (auto* myself = getCppInstance (env, javaInstance))
            myself->addJuceMainComponent (viewContainer);
    }

    static NativeAndroidJuceActivity* getCppInstance (JNIEnv* env, jobject javaInstance)
    {
        // always call JUCE::initialiseJUCEThread in java callbacks
        return reinterpret_cast<NativeAndroidJuceActivity*> (env->GetLongField (javaInstance,
                                                                                NativeAndroidJuceActivityJavaClass.cppCounterpartInstance));
    }

    //==============================================================================
    jweak javaCounterpartInstance = nullptr;
    int counter = 0;
    MainComponent juceMainComponent;
};

NativeAndroidJuceActivity::NativeAndroidJuceActivityJavaClass_Class NativeAndroidJuceActivity::NativeAndroidJuceActivityJavaClass;
