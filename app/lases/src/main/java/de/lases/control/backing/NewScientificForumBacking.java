package de.lases.control.backing;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import de.lases.business.service.ScienceFieldService;
import de.lases.business.service.ScientificForumService;
import de.lases.business.service.UserService;
import de.lases.control.internal.*;
import de.lases.global.transport.*;
import de.lases.control.exception.IllegalAccessException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Event;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing bean for the new scientific forum page.
 *
 * @author Johann Schicho
 */
@ViewScoped
@Named
public class NewScientificForumBacking implements Serializable {

    @Serial
    private static final long serialVersionUID = 6091783394131851720L;

    @Inject
    private Event<UIMessage> uiMessageEvent;

    @Inject
    private transient PropertyResourceBundle propertyResourceBundle;

    @Inject
    private ScienceFieldService scienceFieldService;

    @Inject
    private ScientificForumService scientificForumService;

    @Inject
    private UserService userService;

    @Inject
    private SessionInformation sessionInformation;

    private List<ScienceField> scienceFields;

    private ScientificForum newScientificForum;

    private ScienceField scienceFieldSelectionInput;

    private ScienceField newScienceFieldInput;

    private List<ScienceField> selectedScienceFields;

    private List<User> editors;

    private User newEditorInput;

    /**
     * Initialize dtos and load data from the datasource where necessary.
     * Create dtos for:
     * <ul>
     *     <li> The list of science fields in the system </li>
     *     <li> The new scientific forum that is created on this page </li>
     *     <li> the input for the science field selection </li>
     *     <li> the input for the science field creation </li>
     *     <li> the list of selected science fields </li>
     *     <li> The list of editors </li>
     *     <li> the input for a new editor </li>
     * </ul>
     * Additionally, load the list of science fields in the system from the
     * datasource.
     *
     * @throws IllegalAccessException If the accessing user is not an admin.
     */
    @PostConstruct
    public void init() {
        newScientificForum = new ScientificForum();
        newEditorInput = new User();
        editors = new ArrayList<>();

        scienceFieldSelectionInput = new ScienceField();
        selectedScienceFields = new LinkedList<>();
        scienceFields = scienceFieldService.getList(new ResultListParameters());
    }

    /**
     * Add the editor that is currently entered to the list of editors.
     */
    public void addEditor() {
        User userToAdd = userService.get(newEditorInput);
        if (userToAdd == null) {
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("newForumUserUserNotExists"), MessageCategory.ERROR));
        } else if (editors.contains(userToAdd)) {
            uiMessageEvent.fire(new UIMessage(propertyResourceBundle.getString("newForumUserAlreadyAdded"), MessageCategory.INFO));
        } else {
            editors.add(userToAdd);
        }
        // reset new editor
        newEditorInput = new User();
    }

    /**
     * Remove a specific user from the list of editors.
     *
     * @param editor Editor to remove.
     */
    public void removeEditor(User editor) {
        editors.remove(editor);
    }

    public void removeSelectedScienceField(ScienceField scienceField) {
        selectedScienceFields.remove(scienceField);
    }

    /**
     * Add the science field that is currently entered to the global list of
     * science fields.
     */
    public void addNewScienceField() {
    }

    /**
     * Add the currently selected science field to the list of science fields
     * for this scientific forum.
     */
    public void addScienceField(ScienceField scienceField) {
        System.out.println("adding" + scienceField);
        if (scienceField != null) {
            selectedScienceFields.add(scienceField);
        }
    }

    /**
     * Create the scientific forum and show its overview page.
     *
     * @return Go to the overview page of the crated scientific forum.
     */
    public String create() {
        return null;
    }

    /**
     * Abort the creation of a new scientific forum and return to the
     * administration page.
     *
     * @return Go to the administration page.
     */
    public String abort() {
        return "/views/authenticated/homepage.xhtml";
    }

    /**
     * Get the list of science fields in the system.
     *
     * @return the lsit of science fields in the system.
     */
    public List<ScienceField> getScienceFields() {
        return scienceFields;
    }

    /**
     * Set the list of science fields in the system.
     *
     * @param scienceFields The list of science fields in the system.
     */
    public void setScienceFields(List<ScienceField> scienceFields) {
        this.scienceFields = scienceFields;
    }

    /**
     * Get the dto that encapsulates the parameters for the new scientific
     * forum.
     *
     * @return The dto that encapsulates the parameters for the new scientific
     * forum.
     */
    public ScientificForum getNewScientificForum() {
        return newScientificForum;
    }

    /**
     * Set the dto that encapsulates the parameters for the new scientific
     * forum.
     *
     * @param newScientificForum The dto that encapsulates the parameters
     *                           for the new scientific forum.
     */
    public void setNewScientificForum(ScientificForum newScientificForum) {
        this.newScientificForum = newScientificForum;
    }

    /**
     * Get the science field that the administrator has entered in order to add
     * it to the list of selected science fields for this forum.
     *
     * @return The science field the administrator
     * has entered.
     */
    public ScienceField getScienceFieldSelectionInput() {
        return scienceFieldSelectionInput;
    }

    /**
     * Set the science field that the administrator has entered in order to add
     * it to the list of selected science fields for this forum.
     *
     * @param scienceFieldSelectionInput The science field the administrator has
     *                                   entered.
     */
    public void setScienceFieldSelectionInput(
            ScienceField scienceFieldSelectionInput) {
        this.scienceFieldSelectionInput = scienceFieldSelectionInput;
    }

    /**
     * Ger the science field that the administrator has entered in order to add
     * it to the list of global science fields.
     *
     * @return The science field the administrator has
     * entered.
     */
    public ScienceField getNewScienceFieldInput() {
        return newScienceFieldInput;
    }

    /**
     * Set the science field that the administrator has entered in order to add
     * it to the list of global science fields.
     *
     * @param newScienceFieldInput The science field the administrator has
     *                             entered.
     */
    public void setNewScienceFieldInput(ScienceField newScienceFieldInput) {
        this.newScienceFieldInput = newScienceFieldInput;
    }

    /**
     * Get the list of science fields that are selected for this scientific
     * forum.
     *
     * @return The list of science fields that are selected for this scientific
     * forum.
     */
    public List<ScienceField> getSelectedScienceFields() {
        return selectedScienceFields;
    }

    /**
     * Get the list of users that are currently added as editors to this
     * scientific forum.
     *
     * @return The list of users that are currently added as editors to this
     * scientific forum.
     */
    public List<User> getEditors() {
        return editors;
    }

    /**
     * Get the user that the administrator wants to add to the list of editors.
     *
     * @return The user that the administrator wants to add to
     * the list of editors.
     */
    public User getNewEditorInput() {
        return newEditorInput;
    }

    /**
     * Set the user that the administrator wants to add to the list of editors.
     *
     * @param newEditorInput The user that the administrator wants to add to
     *                       the list of editors.
     */
    public void setNewEditorInput(User newEditorInput) {
        this.newEditorInput = newEditorInput;
    }
}
